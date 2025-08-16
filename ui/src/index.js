const messageClient = require('./entity/message_client')
const express = require('express')
const pug = require('pug')
const message = require('./entity/message')
const bodyParser = require('body-parser')
const PropertiesReader = require('properties-reader')
const props = PropertiesReader('src/resources/application.properties')

const app = express()
app.use(bodyParser.json())
const port = props.get('app.port')

app.set('view engine', 'pug')

app.use(
  express.static('public', {
    setHeaders: (res, path) => {
      if (path.endsWith('.css')) {
        res.setHeader('Content-Type', 'text/css')
      }
    }
  })
)

app.get('/', async (req, res) => {
  let response
  try {
    response = await runClient(
      JSON.stringify(new message.Message('GET_TOPICS', null, null))
    )
    if (isJsonResponseError(response)) {
      console.error('Error on orchestrator side')
      res.render('error')
      return
    }
  } catch (error) {
    console.error(error)
    res.status(500).end()
    return
  }
  console.log(`Response returned ${response}`)
  res.render('topics', { topics: JSON.parse(response) })
})

app.get('/getTopic/:topic', async (req, res) => {
  res.redirect(`/api/topicDetails/${req.params['topic']}`)
})

app.get('/api/topicDetails/:topic', async (req, res) => {
  response = await runClient(
    JSON.stringify(new message.Message('READ', req.params['topic'], null))
  )
  if (isJsonResponseError(response)) {
    console.error('Error on orchestrator side')
    res.render('error')
    return
    /*res.status(500).end()
    return*/
  }
  res.render('messages', {
    messages: JSON.parse(response),
    topicName: req.params['topic']
  })
})

app.post('/api/postMessage', async (req, res) => {
  const jsonData = req.body
  runClient(
    JSON.stringify(
      new message.Message(
        'PUBLISH',
        jsonData.topic,
        jsonData.message,
        jsonData.key
      )
    )
  )
  res.status(200).end()
})

app.post('/api/topic', async (req, res) => {
  const jsonData = req.body
  runClient(JSON.stringify(new message.Message('CREATE_TOPIC', jsonData.topic)))
  res.status(200).end()
})

function isJsonResponseError (rawString) {
  if (rawString === undefined) {
    return true
  }
  parsedJson = JSON.parse(rawString)
  return (
    parsedJson !== undefined &&
    parsedJson.content !== undefined &&
    parsedJson === 'ERROR'
  )
}

app.listen(port, () => {
  console.log(`Safka UI app listening on port ${port}`)
})

async function runClient (jsonMessage) {
  const host = props.get('orchestrator.url')
  const port = props.get('orchestrator.port')

  const client = new messageClient.MessageClient(host, port)

  try {
    await client.connect()

    const readResponse = await client.sendMessage(jsonMessage).then(x => {
      return x
    })
    return readResponse
  } catch (err) {
    console.error('Error:', err)
  } finally {
    client.close()
  }
}
