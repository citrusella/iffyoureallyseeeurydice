package io.github.citrusella.iffyoureallyseeeurydice

class NameList {
    fun buildLabelList(file: ByteArray): MutableMap<String, String> {
        val nameMap = mutableMapOf("FFFFFFFF" to "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
        val byteArray = HexFormat {
            bytes
        } // byte array
        val fileString = file.toHexString()
        val nameMagic = "454d414e"
        var chunkOffset = 128
        var chunkTypeBytes: String
        var chunkType: String
        var lengthBytes: String
        var length: Int
        var inputChunk: String
        //var dataLength: Int
        while (chunkOffset < fileString.length) {
            val lengthOffset = chunkOffset + 8 //length offset
            val lengthEnd = lengthOffset + 8 //end of length
            chunkTypeBytes = fileString.substring(
                chunkOffset,
                lengthOffset
            ) //get chunk type (i.e. BHAV, OBJD, DGRP, etc.)
            chunkType = chunkTypeBytes.hexToByteArray(byteArray)
                .decodeToString() // chunk type as string, used only to find NAME
            lengthBytes =
                fileString.substring(lengthOffset, lengthEnd) // length in bytes from chunk header
            length = lengthBytes.hexToInt() // length as int
            length = length.times(2) // multiply by 2 for string length
            //dataLength = length.div(2) - 16 // subtract header length from data length
            inputChunk = try {
                fileString.substring(chunkOffset, chunkOffset + length) // get to next chunk
            } catch (_: Exception) {
                fileString.substring(chunkOffset) // assume last chunk if try block throws exception
            }
            println(chunkOffset)
            if (chunkType == "NAME" && length > 32) {
                var id: String
                var lengthLabel: Int
                var stringLength: Int
                var label: String
                var remainingChunk = inputChunk.substring(32, length)
                val magicWord = try {
                    remainingChunk.substring(16, 24)
                } catch (_: Exception) {
                    "false"
                }
                if (magicWord.equals(nameMagic,ignoreCase = true)) {
                    remainingChunk = remainingChunk.substring(32)
                    val startIndex = 8
                    while (remainingChunk.length > 8) {
                        id = remainingChunk.take(8)
                        label = remainingChunk.substring(startIndex).substringBefore("00")
                        if (label.isEmpty()) {
                            id = remainingChunk.substring(2,10)
                            label = remainingChunk.substring(10).substringBefore("00")
                            if (label.length % 2 != 0) label += "0"
                        } else {
                            if (label.length % 2 != 0) label += "0"
                        }
                        val labelPlusNullByte = label + "00"
                        remainingChunk = remainingChunk.substringAfter(labelPlusNullByte)

                        println("ID: $id LABEL: ${label.hexToByteArray().decodeToString()}")

                        nameMap[id] = label
                    }
                } else {
                    val startIndex = 10
                    while (remainingChunk.length > 10) {
                        lengthLabel = remainingChunk.substring(8, 10).hexToInt()
                        stringLength = lengthLabel.times(2)

                        id = remainingChunk.take(8)
                        label = remainingChunk.substring(startIndex, startIndex + stringLength)

                        println("ID: $id LENGTH: $lengthLabel LABEL: ${label.hexToByteArray().decodeToString()}")

                        nameMap[id] = label
                        remainingChunk = remainingChunk.substringAfter(label)
                        //Thread.sleep(20000)
                    }
                }
            }
            try {
                chunkOffset += length // get to next offset
            } catch (_: Exception) {
                chunkOffset =
                    fileString.length // if offset is bigger than entire iff length, exception gets thrown and offset is set to iff length and loop stops running
            }
        }
        return nameMap
    }
}