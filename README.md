# Ethereum Wallet Address Generator (with prefix)

This application generates Ethereum addresses with customizable prefixes. It utilizes multithreading to maximize the address generation speed.

## Prerequisites

To run this application, make sure you have Java 1.8 installed

## Usage

Clone the repository:

  ```bash
  git clone https://github.com/alessandrotedd/EthWallet.git
  ```

or

Download the latest release from [here](https://github.com/alessandrotedd/EthWallet/releases).

## Commands

- Generate a random address not encrypted:
  ```bash
  java -jar EthWallet-<version>.jar generate
  ```
- Generate a random address encrypted using a key:
  ```bash
  java -jar EthWallet-<version>.jar generate-encrypted --key <encryption-key>
  ```
- Generate a random address with a prefix not encrypted:
  ```bash
  java -jar EthWallet-<version>.jar generate --prefix <prefix>
  ```
- Generate a random address with a prefix encrypted using a key:
  ```bash
  java -jar EthWallet-<version>.jar generate-encrypted --prefix <prefix> --key <encryption-key>
  ```
- Decrypt a string using a key:
  ```bash
  java -jar EthWallet-<version>.jar decrypt --string <encrypted-string> --key <encryption-key>
  ```
- Encrypt a string using a key:
  ```bash
  java -jar EthWallet-<version>.jar encrypt --string <string> --key <encryption-key>
  ```
- Show version:
  ```bash
  java -jar EthWallet-<version>.jar --version
  ```
  or
  ```bash
  java -jar EthWallet-<version>.jar -v
  ```
- Adding ```--hexize``` to any command with ```--prefix``` will convert the prefix input to hex (example: ```hello``` > ```4e770```):
  ```bash
  java -jar EthWallet-<version>.jar generate --prefix <prefix> --hexize
  ```
- Show help:
  ```bash
  java -jar EthWallet-<version>.jar --help
  ```
## Important Note

Generating addresses with specific prefixes involves a random process. The time taken to find an address with the desired prefix can vary significantly depending on the prefix length and available system resources.
  
The application utilizes the available processors on your system to run address generation in parallel. You can adjust the `numThreads` variable in the code to control the number of threads used.

## License

This project is licensed under the [MIT License](https://opensource.org/license/mit/).

