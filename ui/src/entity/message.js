class Message{
    constructor(kafkaCommand, topicName, contents, key, accessToken){
        this.kafkaCommand = kafkaCommand
        this.topicName = topicName
        this.contents = contents
        this.key = key
        this.accessToken = accessToken
    }
}

module.exports = {
    Message
}