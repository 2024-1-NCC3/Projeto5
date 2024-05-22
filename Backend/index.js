const express = require("express");
const bodyParser = require("body-parser");
const jwt = require("jsonwebtoken");
const sqlite3 = require("sqlite3").verbose();

const app = express();
const port = process.env.PORT || 3000;
const secretKey = "seu_segredo";
const db = new sqlite3.Database("BeatCard.db");

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Middleware para autenticação do token
const authenticateToken = (req, res, next) => {
  const authHeader = req.headers["authorization"];
  const token = authHeader && authHeader.split(" ")[1];

  if (!token) return res.sendStatus(401);

  jwt.verify(token, secretKey, (err, user) => {
    if (err) return res.sendStatus(403);
    req.user = user;
    next();
  });
};

// Rota inicial
app.get("/", (req, res) => {
  console.log("GET /");
  res.send("Hello World!");
});

// Rota para criar um novo usuário
app.post("/user", (req, res) => {
  const { email, senha } = req.body;

  if (!email || !senha) {
    return res.status(400).json({ error: "Email e senha são obrigatórios" });
  }

  db.get("SELECT * FROM Usuarios WHERE email = ?", [email], (err, row) => {
    if (err) {
      console.error("Erro ao verificar email:", err.message);
      return res.status(500).json({ error: "Erro interno do servidor" });
    }

    if (row) {
      return res.status(400).json({ error: "Este email já está em uso" });
    }

    db.run(
      "INSERT INTO Usuarios (email, senha) VALUES (?, ?)",
      [email, senha],
      (err) => {
        if (err) {
          console.error("Erro ao inserir usuário:", err.message);
          return res.status(500).json({ error: "Erro interno do servidor" });
        }

        res.status(201).json({ message: "Usuário criado com sucesso" });
      },
    );
  });
});

// Rota de login
app.post("/login", (req, res) => {
  const { email, senha } = req.body;

  db.get(
    "SELECT * FROM Usuarios WHERE email = ? AND senha = ?",
    [email, senha],
    (err, row) => {
      if (err) {
        return res.status(500).json({ error: "Erro interno do servidor" });
      }

      if (row) {
        const token = jwt.sign({ id: row.id, email: row.email }, secretKey, {
          expiresIn: "1h",
        });
        res.json({
          success: true,
          message: "Login bem-sucedido",
          token: token,
        });
      } else {
        res
          .status(401)
          .json({ success: false, message: "Credenciais inválidas" });
      }
    },
  );
});

// Exemplo de rota protegida
app.get("/dados-protegidos", authenticateToken, (req, res) => {
  res.json({ success: true, message: "Dados protegidos", user: req.user });
});

// Rota para excluir um usuário
app.delete("/user/:id", authenticateToken, (req, res) => {
  const userId = req.params.id;

  if (!userId) {
    return res.status(400).json({ error: "O ID do usuário é obrigatório" });
  }

  db.run("DELETE FROM Usuarios WHERE id = ?", [userId], (err) => {
    if (err) {
      console.error("Erro ao excluir usuário:", err.message);
      return res.status(500).json({ error: "Erro interno do servidor" });
    }

    res.status(200).json({ message: "Usuário excluído com sucesso" });
  });
});

// Rota para selecionar todos os usuários
app.get("/selectusuario", authenticateToken, (req, res) => {
  db.all("SELECT * FROM Usuarios", [], (err, rows) => {
    if (err) {
      console.log(err);
      return res.status(500).json({ error: "Erro ao buscar usuários" });
    }

    res.json(rows);
  });
});

// Rota para selecionar todos os cards (deve ser protegida)
app.get("/selectcard", authenticateToken, (req, res) => {
  db.all("SELECT * FROM Cards", [], (err, rows) => {
    if (err) {
      console.log(err);
      return res.status(500).json({ error: "Erro ao buscar cards" });
    }

    res.json(rows);
  });
});

// Rota para obter cards do usuário autenticado
app.get("/getcard", authenticateToken, (req, res) => {
  const userId = req.user.id;

  db.all("SELECT * FROM Cards WHERE usuario_id = ?", [userId], (err, rows) => {
    if (err) {
      console.log(err);
      return res.status(500).json({ error: "Erro ao buscar cards" });
    }

    res.json(rows);
  });
});

// Rota para criar um novo card
app.post("/cards", authenticateToken, (req, res) => {
  const { titulo, conteudo, nivel_esquecimento } = req.body;
  const userId = req.user.id;

  if (!titulo || !conteudo || !nivel_esquecimento) {
    return res.status(400).json({ error: "Todos os campos são obrigatórios" });
  }

  db.run(
    "INSERT INTO Cards (usuario_id, titulo, conteudo, nivel_esquecimento) VALUES (?, ?, ?, ?)",
    [userId, titulo, conteudo, nivel_esquecimento],
    function (err) {
      if (err) {
        console.error("Erro ao inserir card:", err.message);
        return res.status(500).json({ error: "Erro interno do servidor" });
      }

      res.status(201).json({ message: "Card criado com sucesso" });
    },
  );
});

// Rota para atualizar um card
app.put("/cards/:id", authenticateToken, (req, res) => {
  const cardId = req.params.id;
  const { titulo, conteudo, nivel_esquecimento } = req.body;

  if (!cardId) {
    return res.status(400).json({ error: "O ID do card é obrigatório" });
  }

  if (!titulo && !conteudo && !nivel_esquecimento) {
    return res
      .status(400)
      .json({ error: "Pelo menos um campo a ser atualizado é obrigatório" });
  }

  let updateQuery = "UPDATE Cards SET";
  let params = [];
  if (titulo) {
    updateQuery += " titulo = ?,";
    params.push(titulo);
  }
  if (conteudo) {
    updateQuery += " conteudo = ?,";
    params.push(conteudo);
  }
  if (nivel_esquecimento) {
    updateQuery += " nivel_esquecimento = ?,";
    params.push(nivel_esquecimento);
  }

  updateQuery = updateQuery.slice(0, -1);
  updateQuery += " WHERE id = ?";
  params.push(cardId);

  db.run(updateQuery, params, function (err) {
    if (err) {
      console.error("Erro ao atualizar card:", err.message);
      return res.status(500).json({ error: "Erro interno do servidor" });
    }

    res.status(200).json({ message: "Card atualizado com sucesso" });
  });
});

// Rota para excluir um card
app.delete("/cards/:id", authenticateToken, (req, res) => {
  const cardId = req.params.id;

  // Verifica se o ID do card foi fornecido
  if (!cardId) {
    return res.status(400).json({ error: "O ID do card é obrigatório" });
  }

  // Exclui o card do banco de dados
  db.run("DELETE FROM Cards WHERE id = ?", [cardId], function (err) {
    if (err) {
      console.error("Erro ao excluir card:", err.message);
      return res.status(500).json({ error: "Erro interno do servidor" });
    }

    // Verifica se algum registro foi realmente excluído
    if (this.changes > 0) {
      res.status(200).json({ message: "Card excluído com sucesso" });
    } else {
      res.status(404).json({ error: "Card não encontrado" });
    }
  });
});

app.listen(port, () => {
  console.log(`Servidor rodando na porta ${port}`);
});
