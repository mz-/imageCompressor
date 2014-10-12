import java.awt.image.BufferedImage
import java.lang.StringBuilder
import java.io.*
import java.util.*


object imageCompressor {
	def main(args: Array[String]) = {
		if (args.length != 3) {
			println("[encode/decode] [original file] [destination]")
			System.exit(1)
		} else {
			if (args[0] == "encode") {
				compressImage(getImage(args[1]), args[2])
			} else if (args[0] == "decode") {
				decodeImage(args[1], args[2])
			}
		}
	}


	def getImage(file: String): BufferedImage = {
		//Returns BufferedImage given a filename or file path
		var BufferedImage img = null;
		try {
			img = ImageIO.read(new File(file))
		} catch (IOException e) {
			System.println("Error reading file")
		}
		return img
	}

	def compressImage(image: BufferedImage, loc: String): String = {
		val maxX = image.getWidth()
		val maxY = image.getHeight()

		var currPixel = image.getRGB(0, 0)
		var currCount = 0

		var StringBuilder returnString = new StringBuilder()
		returnString.append(maxX + "$" + maxY + "$")
		for (x <- 0 to (maxX - 1); y <- 0 to (maxY - 1)) {
			if (image.getRGB(x, y) == currPixel) {
				currCount++
			} else {
				returnString.append(currPixel + "." + currCount + "!")
				currPixel = thisPixel
				currCount = 1
			}
		}
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(loc))
			output.write(returnString.toString())
			output.close()
		} catch {
			case ioe: IOException => println("Error writing to file")
		}
	}

	def decodeImage(str: String, loc: String) = {
		val width = getWidth(str)
		val height = getHeight(str)
		val resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		val parsedStr = getData(str).split("!")

		var currIndex = 0
		var currX = 0
		var currY = 0
		var currNum = 0

		while (currIndex < parsedStr.size) {
			var currRGB = getValue(parsedStr[currIndex])
			var currFreq = getFreq(parsedStr[currIndex])
			currNum = 0
			while (currNum <= currFreq) {
				if (currX >= width) {
					currX = 0
					currY++
				}
				resultImage.setRGB(currX, currY, currRGB)
				currNum++
				currX++
			}
			currIndex++

		}
		try {
			ImageIO.write(resultImage, "PNG", new File(loc))
		} catch {
			case ioe: IOException => println("Error reading file")
		}
	}

	//Abstraction issues here. Replace with better written get methods
	def getValue(data: String): Int = {
		return Integer.parseInt(data.split(".")[0])
	}

	def getFreq(data: String): Int = {
		return Integer.parseInt(data.split(".")[1])
	}

	def getWidth(data: String): Int = {
		return Integer.parseInt(data.split("$")[0])
	}

	def getHeight(data: String): Int = {
		return Integer.parseInt(data.split("$")[1])
	}

	def getData(data: String): String = {
		return data.split("$")[2]
	}
}

