const messageClient = require('./entity/message_client');
const express = require('express');
const pug = require('pug');
const message = require('./entity/message');
const bodyParser = require('body-parser');

const app = express()
app.use(bodyParser.json());
const port = 3000

app.set('view engine', 'pug')

app.get('/', async (req, res) => {
    response = await runClient(JSON.stringify(new message.Message('GET_TOPICS', null, null)))
    console.log(`Response returned ${response}`);
    res.render('topics', { topics: JSON.parse(response) });
})

app.get('/getTopic/:topic', async (req, res) => {
    res.redirect(`/api/topicDetails/${req.params['topic']}`)
})

app.get('/api/topicDetails/:topic', async (req, res) => {
    response = await runClient(JSON.stringify(new message.Message('READ', req.params['topic'], null)))
    console.log(`Response returned ${response}`);
    res.render('messages', { messages: JSON.parse(response) });
})

app.post('/api/postMessage', async (req, res) => {
  const jsonData = req.body;
  response = await runClient(JSON.stringify(new message.Message('PUBLISH', jsonData.topic, jsonData.message)))
  res.status(200).end()
})

app.listen(port, () => {
    console.log(`Safka UI app listening on port ${port}`)
})

async function runClient(jsonMessage) {
  const host = 'localhost'; // Replace with your orchestrator's host
  const port = 7500; // Replace with your orchestrator's port

  const client = new messageClient.MessageClient(host, port);

  try {
    await client.connect();

    const readResponse = await client.sendMessage(jsonMessage).then((x) => {return x});
    return readResponse;
  } catch (err) {
    console.error('Error:', err);
  } finally {
    client.close();
  }
}