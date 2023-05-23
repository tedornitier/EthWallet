package com.github.alessandrotedd.ethwallet

import com.github.alessandrotedd.ethwallet.utils.decryptString
import com.github.alessandrotedd.ethwallet.utils.encryptString
import com.github.alessandrotedd.ethwallet.utils.generatePrivateKey
import com.github.alessandrotedd.ethwallet.utils.hexize
import kotlinx.coroutines.Dispatchers.Main
import java.io.File
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun main(args: Array<String>) {
    var prefix = ""
    var key: String? = null
    var encryptionKey: String? = null
    var decryptionKey: String? = null

    args.forEachIndexed { index, arg ->
        when (arg) {
            "--prefix", "-p" -> args.getOrNull(index + 1)?.let { prefix = it } ?: run { showHelp(); return@main }
            "--key", "-k" -> args.getOrNull(index + 1)?.let { key = it } ?: run { showHelp(); return@main }
            "--encrypt", "-e" -> args.getOrNull(index + 1)?.let { encryptionKey = it } ?: run { showHelp(); return@main }
            "--decrypt", "-d" -> args.getOrNull(index + 1)?.let { decryptionKey = it } ?: run { showHelp(); return@main }
            "--help", "-h" -> { showHelp(); return@main }
        }
    }

    encryptionKey?.let { k ->
        println("Enter the string to encrypt:")
        val input = readlnOrNull()
        input?.let {
            println("Encrypted string: ${encryptString(it, k)}")
        } ?: run {
            println("Invalid input")
        }
        return@main
    }

    decryptionKey?.let { k ->
        println("Enter the string to decrypt:")
        val input = readlnOrNull()
        input?.let {
            println("Decrypted string: ${decryptString(it, k)}")
        } ?: run {
            println("Invalid input")
        }
        return@main
    }

    println(when {
        prefix.isEmpty() && key == null -> "Generating random private key"
        prefix.isEmpty() && key != null -> "Generating random private key and encrypting it with key \"$key\""
        prefix.isNotEmpty() && key == null -> "Generating private key for addresses starting with: $prefix"
        prefix.isNotEmpty() && key != null -> "Generating private key for addresses starting with: $prefix and encrypting it with key \"$key\""
        else -> ""
    })

    generatePrivateKey(hexize(prefix)).let { wallet ->
        val address = wallet.first
        val privateKey = wallet.second
        println("Address: 0x$address")
        key?.let {
            encryptString(privateKey, it)
        } ?: run {
            privateKey
        }
    }.also { privateKey ->
        println("Private key ${
            if (key != null) "encrypted with key \"$key\""
            else "not encrypted"
        }: $privateKey")
    }
}

fun getJarFileName(): String {
    val path = Main::class.java.protectionDomain.codeSource.location.path
    val decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8)
    val file = File(decodedPath)
    return file.name
}

fun showHelp() {
    val jarFileName = getJarFileName()

    println("Usage: java -jar $jarFileName <command> [options]")
    println("Available commands:")
    println("- Generate a random address not encrypted:")
    println("  java -jar $jarFileName generate")

    println("- Generate a random address encrypted using a key:")
    println("  java -jar $jarFileName generate-encrypted --key <encryption-key>")

    println("- Generate a random address with a prefix not encrypted:")
    println("  java -jar $jarFileName generate --prefix <prefix>")

    println("- Generate a random address with a prefix encrypted using a key:")
    println("  java -jar $jarFileName generate-encrypted --prefix <prefix> --key <encryption-key>")

    println("- Decrypt a string using a key:")
    println("  java -jar $jarFileName decrypt --string <encrypted-string> --key <encryption-key>")

    println("- Encrypt a string using a key:")
    println("  java -jar $jarFileName encrypt --string <string> --key <encryption-key>")

    println("- Show help:")
    println("  java -jar $jarFileName --help or java -jar $jarFileName -h")
}
