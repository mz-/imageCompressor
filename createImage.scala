import java.awt.image.BufferedImage
import java.lang.StringBuilder
import util.Random

object createImage {
	def main(args: Array[String]) = {
		//TODO
	}

	def create(color: Int, width: Int, height: Int, location: String, random: Boolean) {
		val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

		for (x <- width - 1; y <- height - 1) {
			if (random) {
				color = Random.nextInt(256)
			}
			img.setRGB(x, y, color)
		}
		try {
			ImageIO.write(img, "PNG", new File(loc))
		} catch {
			case ioe: IOException => println("Error writing to file")
		}
	}


}
