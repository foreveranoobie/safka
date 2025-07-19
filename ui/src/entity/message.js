class Message{
    constructor(kafkaCommand, topicName, contents, key){
        this.kafkaCommand = kafkaCommand
        this.topicName = topicName
        this.contents = contents
        this.key = key
    }
}

module.exports = {
    Message
}