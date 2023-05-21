import org.web3j.crypto.Keys
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import kotlin.concurrent.thread
import kotlin.math.pow

fun main() {
    println(generatePrivateKey(hexize("caffe")))
}

fun generatePrivateKey(vararg addressPrefixes: String): String {
    println("Generating private key for addresses starting with: ${addressPrefixes.joinToString(", ")}")
    val possibleChoices = 16.0.pow(addressPrefixes[0].length)
    println("Possible choices for #1: ${possibleChoices.toInt()}")

    val queue = ConcurrentLinkedQueue<String>()
    val numThreads = Runtime.getRuntime().availableProcessors()
    val mutex = ReentrantLock()

    var startTime = System.currentTimeMillis()
    val addressesGenerated = AtomicInteger(0)

    val threadPool = Array(numThreads) {
        thread {
            while (true) {
                val ecKeyPair = Keys.createEcKeyPair()
                val privateKey = ecKeyPair.privateKey.toString(16).padStart(64, '0')
                val address = Keys.getAddress(ecKeyPair.publicKey)

                addressesGenerated.incrementAndGet()
                if (System.currentTimeMillis() - startTime > 1000) {
                    mutex.lock()
                    if (System.currentTimeMillis() - startTime > 1000) {
                        val elapsedTime = System.currentTimeMillis() - startTime
                        val addressesPerSecond = addressesGenerated.get() * 1000 / elapsedTime
                        println("Addresses per second: $addressesPerSecond, total time estimate: ${possibleChoices / addressesPerSecond.toInt()} seconds")
                        addressesGenerated.set(0)
                        startTime = System.currentTimeMillis()
                    }
                    mutex.unlock()
                }

                for (addressPrefix in addressPrefixes) {
                    if (address.startsWith(addressPrefix)) {
                        val result = Pair(address, privateKey).toString()
                        queue.add(result)
                        return@thread
                    }
                }
            }
        }
    }

    threadPool.forEach { it.join() }
    return queue.poll()!!
}

fun hexize(input: String): String {
    val substitutionMap = mapOf(
        'A' to 4,
        'B' to 8,
        'D' to 0,
        'E' to 3,
        'a' to 'a',
        'b' to 'b',
        'c' to 'c',
        'd' to 'd',
        'e' to 'e',
        'f' to 'f',
        'g' to '9',
        'h' to '4',
        'i' to '1',
        'j' to '7',
        'l' to '7',
        'o' to '0',
        'q' to '9',
        'r' to '2',
        's' to '5',
        't' to '7',
        'z' to '2'
    )

    val unreplaceableChars = setOf('k', 'm', 'n', 'p', 'u', 'v', 'w', 'x', 'y')
    val containsIrreplaceableChar = unreplaceableChars.any { input.contains(it) }
    if (containsIrreplaceableChar) {
        throw IllegalArgumentException("Input string contains an irreplaceable character: ${unreplaceableChars.find { input.contains(it) }}")
    }

    return input.map { substitutionMap[it] ?: it }.joinToString("")
}

fun encryptString(string: String, key: String): String {
    val secretKey = generateSecretKey(key)
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val encrypted = cipher.doFinal(string.toByteArray())
    return Base64.getEncoder().encodeToString(encrypted)
}

fun decryptString(string: String, key: String): String {
    val cipher = Cipher.getInstance("AES")
    val secretKey = generateSecretKey(key)
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    val decrypted = cipher.doFinal(Base64.getDecoder().decode(string))
    return String(decrypted)
}

fun generateSecretKey(key: String): SecretKey {
    val keyBytes = key.toByteArray()
    val sha = MessageDigest.getInstance("SHA-256")
    val keySpec = sha.digest(keyBytes)
    return SecretKeySpec(keySpec, "AES")
}
