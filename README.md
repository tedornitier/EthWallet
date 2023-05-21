# Ethereum Wallet Address Generator (with prefix)

This application generates Ethereum addresses with customizable prefixes. It utilizes multithreading to maximize the address generation speed.

## Prerequisites

To run this application, make sure you have the following installed:

- Java Development Kit (JDK)
- Kotlin

## Usage

1. Clone the repository:

    ```bash
    git clone https://github.com/alessandrotedd/EthWallet.git
    ```
2. Open the project in your preferred IDE.

3. Run the `main` function in the `Main.kt` file.

4. Specify the desired prefix by modifying the `addressPrefix` parameter in the `generatePrivateKey` function call.

5. The application will start generating private keys and Ethereum addresses that match the specified prefix.

## Configuration

The following configuration options are available:

- Number of Threads: The application utilizes the available processors on your system to run address generation in parallel. You can adjust the `numThreads` variable in the code to control the number of threads used.

## Important Note

Generating addresses with specific prefixes involves a random process. The time taken to find an address with the desired prefix can vary significantly depending on the prefix length and available system resources.

## License

This project is licensed under the [MIT License](https://opensource.org/license/mit/).

