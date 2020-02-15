import java.util.Scanner
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing._
import java.io._
import javax.imageio.ImageIO;
import javax.imageio._;
import java.awt.GridLayout
import java.awt.FlowLayout
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.image._



object MainGUI {
	def main(args: Array[String]): Unit = {


			//*********************************** Reverse Image Color ****************************************
			inerseImg()
			//*********************************** Mirror Image ****************************************
			mirrorImg()

			//*********************************** GUI ****************************************
			var originalImg=RgbPixelMap.loadingImgMap(filename="House256.ppm").get 
			var originaLabel = new JLabel(new ImageIcon(originalImg.image))
			val frame = new JFrame("Image Processing using Quadtree")
			frame.setSize(new Dimension(900, 700))

			// ****************Buttons************************
			val originalButton = new JButton("Original Image")
			val inverseButton = new JButton("Reverse Color")	
			val mirrorButton = new JButton("Mirror Image")
			val rotateButton = new JButton("Rotate Image")

			// ****************Panels************************

			val panel1 = new JPanel()
			val panel2 = new JPanel()
		

			panel1.setLayout(new FlowLayout())
			

			panel1.add(originalButton)
			panel1.add(inverseButton)
			panel1.add(mirrorButton)
			panel1.add(rotateButton)


			panel2.add(originaLabel)

			// ****************Perform Actions on Buttons************************

			inverseButton.addActionListener(new ActionListener {
				override def actionPerformed(e: ActionEvent): Unit = {
						var inverseImg=RgbPixelMap.loadingImgMap(filename="inverse.ppm").get  
								var	inverseLabel = new JLabel(new ImageIcon(inverseImg.image))
								
								frame.getContentPane.removeAll;

						frame.add(panel1)

						frame.add(new JPanel().add(inverseLabel))
						frame.validate()
						println("inverse")

				}
			})

			mirrorButton.addActionListener(new ActionListener {
				override def actionPerformed(e: ActionEvent): Unit = {
						var mirrorImg=RgbPixelMap.loadingImgMap(filename="mirror.ppm").get
								var	mirrorLabel = new JLabel(new ImageIcon(mirrorImg.image))
								
								frame.getContentPane.removeAll;

						frame.add(panel1)

						frame.add(new JPanel().add(mirrorLabel))
						frame.validate()
						println("mirror")

				}
			})
			var angle = 0.0

			rotateButton.addActionListener(new ActionListener {
				override def actionPerformed(e: ActionEvent): Unit = {
						  
							
						angle=angle+90.0

								MainGUI.rotateImg(angle)
								var rotateImg = RgbPixelMap.loadingImgMap(filename="rotate.ppm").get
								var	rotateLabel = new JLabel(new ImageIcon(rotateImg.image))
                
								frame.getContentPane.removeAll;

	
						frame.add(panel1)
						frame.add(new JPanel().add(rotateLabel) )

						frame.validate()
								println("rotate")
							

				}
			})
			originalButton.addActionListener(new ActionListener {
				override def actionPerformed(e: ActionEvent): Unit = {
						originalImg=RgbPixelMap.loadingImgMap(filename="House256.ppm").get
								originaLabel = new JLabel(new ImageIcon(originalImg.image))
								
								frame.getContentPane.removeAll;

						frame.add(panel1)

						frame.add(new JPanel().add(originaLabel))
						frame.validate()
						println("original")

				}
			})



			frame.setLayout(new GridLayout(2,1))
			frame.getContentPane.add(panel1)
			frame.getContentPane.add(panel2)
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

			frame.setLocationRelativeTo(null)
			frame.setVisible(true)
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())









	}
	/*  Save Inverse Image */
	def inerseImg() : Unit = {
			val img=RgbPixelMap.loadingImgMap(filename="House256.ppm").get
					val width = img.width

					val height = img.height
					val matrixRed = Array.ofDim[Int](width, height)
					val matrixGreen = Array.ofDim[Int](width, height)
					val matrixBlue = Array.ofDim[Int](width, height)

					for( i <- 0 until width) 
					{ 
						for( j <- 0 until height) 
						{
							matrixRed(i)(j) = 255-img.getPixel(i,j).getRed
									matrixGreen(i)(j) = 255-img.getPixel(i,j).getGreen
									matrixBlue(i)(j) = 255-img.getPixel(i,j).getBlue     
									//println(matrixRed(i)(j)+ "  "+ matrixGreen(i)(j)+"  "+matrixBlue(i)(j))


						}  



					}

			/* threshold value is useful to control the pixels of an image */
			val threshold : Float = (30).toFloat


					val quadTreeInverse : PixelGrid = new PixelGrid(0,0,width,height,threshold,matrixRed,matrixGreen,matrixBlue)

					RgbPixelMap.save(width, height, quadTreeInverse, filename="inverse.ppm")
	}
	/*  Save Mirror Image */
	def mirrorImg() : Unit = {
			val img=RgbPixelMap.loadingImgMap(filename="House256.ppm").get
					val width = img.width

					val height = img.height
					val matrixRed = Array.ofDim[Int](width, height)
					val matrixGreen = Array.ofDim[Int](width, height)
					val matrixBlue = Array.ofDim[Int](width, height)

					for( i <- 0 until width) 
					{ 
						for( j <- 0 until height) 
						{
							matrixRed(i)(j) = img.getPixel(i,j).getRed
									matrixGreen(i)(j) = img.getPixel(i,j).getGreen
									matrixBlue(i)(j) = img.getPixel(i,j).getBlue     
									//println(matrixRed(i)(j)+ "  "+ matrixGreen(i)(j)+"  "+matrixBlue(i)(j))


						}  



					}

			/* threshold value is useful to control the pixels of an image */
			val threshold : Float = (30).toFloat


					/*  Save Mirror Image */	
					val quadTreeMirror : PixelGrid = new PixelGrid(0,0,width,height,threshold,matrixRed.reverse,matrixGreen.reverse,matrixBlue.reverse)

					RgbPixelMap.save(width, height, quadTreeMirror, filename="mirror.ppm")
	}
	/*  Save Rotated Image */
	def rotateImg(angleDegree:Double) : Unit = {
			val img=RgbPixelMap.loadingImgMap(filename="House256.ppm").get
					val width = img.width

					val height = img.height
					val matrixRed = Array.ofDim[Int](width, height)
					val matrixGreen = Array.ofDim[Int](width, height)
					val matrixBlue = Array.ofDim[Int](width, height)



					var angle = Math.toRadians(angleDegree)
					var sin = Math.sin(angle)
					var cos = Math.cos(angle)
					var x0 = 0.5 * (width  - 1)     // point to rotate about
					var y0 = 0.5 * (height - 1)     // center of image


					// rotation
					for( i <- 0 until width) 
					{ 
						for( j <- 0 until height) 
						{
							var a = i - x0
									var b = j - y0
									var xx = (a * cos - b * sin + x0).toInt
									var yy = (a * sin + b * cos + y0).toInt

									// plot pixel (x, y) the same color as (xx, yy) if it's in bounds
									if (xx >= 0 && xx < width && yy >= 0 && yy < height) {
										//pic2.set(x, y, pic1.get(xx, yy));
										matrixRed(i)(j) = img.getPixel(xx,yy).getRed
												matrixGreen(i)(j) = img.getPixel(xx,yy).getGreen
												matrixBlue(i)(j) = img.getPixel(xx,yy).getBlue     

									}
						}
					}




			/* threshold value is useful to control the pixels of an image */
			val threshold : Float = (30).toFloat

					/*  Save Mirror Image */	
					val quadTreeMirror : PixelGrid = new PixelGrid(0,0,width,height,threshold,matrixRed,matrixGreen,matrixBlue)

					RgbPixelMap.save(width, height, quadTreeMirror, filename="rotate.ppm")
	}





}