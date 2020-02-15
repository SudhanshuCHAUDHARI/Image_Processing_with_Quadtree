
import java.awt.image._
import java.awt._
import java.io._

object RgbPixelMap extends App {
  class RgbPixelMap(val dimension: (Int, Int)) {
    def width = dimension._1
    def height = dimension._2
    val image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
 
    def apply(x: Int, y: Int) = new Color(image.getRGB(x, y))
 
    def update(x: Int, y: Int, c: Color): Unit = image.setRGB(x, y, c.getRGB)
    
    def getPixel(x: Int, y: Int): Color = this(x, y)
    
    def setPixel(x: Int, y: Int, c: Color): Unit = this(x, y) = c
 
    def fill(c: Color): Unit = {
      val g = image.getGraphics
      g.setColor(c)
      g.fillRect(0, 0, width, height)
    }
  }
  object RgbPixelMap {
    def apply(width: Int, height: Int) = new RgbPixelMap(width, height)
  }
  private case class PpmHeader(format:String, width:Int, height:Int, maxColor:Int)

  //*************** Read the image file***************************
  def loadingImgMap(filename:String):Option[RgbPixelMap]={
    implicit val in=new BufferedInputStream(new FileInputStream(filename))
   // Read the header P6 , width ,height and  maxColor
    val header=readHeader
    if(header.format=="P6")
  /* The P6 binary format of the same image represents each color component of each pixel with one byte (thus three bytes per pixel)
  in the order red, green, then blue. The file is smaller, but the color information is difficult to read by humans.
   */
    {
      val bm=new RgbPixelMap(header.width, header.height);
      for(y <- 0 until bm.height; x <- 0 until bm.width; c=readColor)
        bm.setPixel(x, y, c)
      in.close()
      return Some(bm)
    }
    in.close()
    None
  }

  private def readHeader(implicit in:InputStream):PpmHeader={
    var format=readLine

    var line=readLine
    while(line.startsWith("#"))   //skip comments
      line=readLine

    val parts=line.split("\\s")
    val width=parts(0).toInt
    val height=parts(1).toInt
    val maxColor=readLine.toInt

    new PpmHeader(format, width, height, maxColor)
  }

  private def readColor(implicit in:InputStream)=new Color(in.read, in.read, in.read)

  private def readLine(implicit in:InputStream):String={
    var out=""
    var b=in.read
    while(b!=0xA){out+=b.toChar; b=in.read}
    out
  }
  
  //******************************Write the matrix to image file******************************
  def save(width:Int,height:Int,QuadTree:PixelGrid, filename:String): Unit={
    val out=new DataOutputStream(new FileOutputStream(filename))

    out.writeBytes("P6\u000a%d %d\u000a%d\u000a".format(width, height, 255))

    for(y <- 0 until height; x <- 0 until width){
      out.writeByte(QuadTree.getColor(x, y).red)
      out.writeByte(QuadTree.getColor(x, y).green)
      
      out.writeByte(QuadTree.getColor(x, y).blue)
    }
    out.flush()
    out.close()
  }

}
