package me.Alexqp.kryptoAPI;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MessageEncoder {

    public static List<Character> getDefaultAlphabet() {
        List<Character> alphabet = new ArrayList<>();
        // a,...,z
        for (int i = 97; i < 123; i++) {
            alphabet.add((char) i);
        }
        alphabet.add(' ');
        return alphabet;
    }

    private final int alphabetLength;
    private final int blockLength;
    private final List<Character> alphabet;

    public MessageEncoder(int blockLength) throws IllegalArgumentException {
        this(blockLength, getDefaultAlphabet());
    }

    public MessageEncoder(int blockLength, @NotNull List<Character> alphabet) throws IllegalArgumentException {
        if (blockLength <= 0)
            throw new IllegalArgumentException("plaintextBlockLength must be above 0");
        this.alphabet = new ArrayList<>(alphabet);
        this.alphabetLength = alphabet.size() + 1;
        this.blockLength = blockLength;
    }

    public long getMaxNumber() {
        long result = alphabetLength - 2;
        long n = alphabetLength;
        for (int i = 1; i < blockLength; i++) {
            result += (alphabetLength - 1) * n;
            n = n * alphabetLength;
        }
        return result;
    }

    private boolean checkString(@Nullable String msg) {
        if (msg != null) {
            for (int i = 0; i < msg.length(); i++) {
                if (!alphabet.contains(msg.charAt(i)))
                    return false;
            }
            return true;
        }
        return false;
    }

    @NotNull
    public List<Long> encode(@Nullable String msg) {
        if (!checkString(msg)) throw new IllegalArgumentException("msg is null or contains illegal characters.");
        if (msg.isEmpty()) return new ArrayList<>();
        int lastBlockLength = msg.length() % blockLength;
        int exceptLastBlockLength = msg.length() - lastBlockLength;
        List<Long> result = new ArrayList<>();
        for (int i = 0; i < exceptLastBlockLength; i = i + blockLength) {
            long blockResult = 0;
            for (int j = 0; j < blockLength; j++) {
                blockResult += alphabet.indexOf(msg.charAt(i + j)) * Math.pow(alphabetLength, j);
            }
            result.add(blockResult);
        }
        if (lastBlockLength != 0) {
            long lastBlockResult = 0;
            int j;
            for (j = 0; j < lastBlockLength; j++) {
                lastBlockResult += alphabet.indexOf(msg.charAt(exceptLastBlockLength + j)) * Math.pow(alphabetLength, j);
            }
            for (; j < blockLength; j++) {
                lastBlockResult += (alphabetLength - 1) * Math.pow(alphabetLength, j);
            }
            result.add(lastBlockResult);
        }
        return result;
    }

    @NotNull
    public String decode(@NotNull List<Long> encodedMsg) {
        StringBuilder result = new StringBuilder();
        for (long block : encodedMsg) {
            StringBuilder blockResult = new StringBuilder();
            for (int j = 0; j < blockLength; j++) {
                long c = block % alphabetLength;
                if (c != alphabetLength - 1)
                    blockResult.append(alphabet.get((int) c));
                block = block / alphabetLength;
            }
            result.append(blockResult);
        }
        return result.toString();
    }
}
