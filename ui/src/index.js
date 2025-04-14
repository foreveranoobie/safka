const messageClient = require('./entity/message_client');
const express = require('express');
const pug = require('pug');
const message = require('./entity/message');

const app = express()
const port = 3000

app.set('view engine', 'pug')

app.get('/', async (req, res) => {
    response = await runClient(JSON.stringify(new message.Message('GET_TOPICS', null, null)))
    console.log(`Response returned ${response}`);
    res.render('topics', { topics: JSON.parse(response) });
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