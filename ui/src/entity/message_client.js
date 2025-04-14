const net = require('net');

class MessageClient {
  constructor(host, port) {
    this.host = host;
    this.port = port;
    this.client = null;
  }

  connect() {
    return new Promise((resolve, reject) => {
      this.client = net.createConnection(this.port, this.host, () => {
        console.log('Connected to orchestrator.');
        resolve();
      });

      this.client.on('error', (err) => {
        console.error('Connection error:', err);
        reject(err);
      });

      this.client.on('end', () => {
        console.log('Disconnected from orchestrator.');
      });

      this.client.on('close', () => {
        reject(new Error('Connection closed.'));
      });
    });
  }

  sendMessage(message) {
    return new Promise((resolve, reject) => {
      if (!this.client) {
        reject(new Error('Not connected to orchestrator.'));
        return;
      }

      this.client.write(message + '\n', (err) => { // Add newline for message delimiter
        if (err) {
          reject(err);
        } else {
          this.client.once('data', (data) => { // Use once to get a single response
            resolve(data.toString());
          });
        }
      });
    });
  }

  receiveResponse() {
    return new Promise((resolve, reject) => {
      this.client.once('data', (data) => { // Use once to get a single response
        resolve(data.toString());
      });

      this.client.on('error', reject); // Handle errors during reception
    });
  }

  close() {
    if (this.client) {
      this.client.end();
    }
  }
}

module.exports = {
  MessageClient
}