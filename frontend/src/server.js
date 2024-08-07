const express = require('express');
const app = express();
const port = 2001;

app.get('/verification-status', (req, res) => {
  res.json({ verified: false });
});

app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});