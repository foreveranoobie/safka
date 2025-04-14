class Message{
    constructor(kafkaCommand, topicName, contents){
        this.kafkaCommand = kafkaCommand
        this.topicName = topicName
        this.contents = contents
    }
}

module.exports = {
    Message
}