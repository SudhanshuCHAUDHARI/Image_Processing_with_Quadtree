
class PixelGrid (x: Int,  y: Int,  width: Int,height: Int) {
  var color:Colors = new Colors(red= 0, green= 0, blue= 0)
  var nodeList:Array[PixelGrid] = null

  // Create the QuadTree
  def this(x: Int,  y: Int,  width: Int,height: Int, threshold: Float,mr:Array[Array[Int]], mg:Array[Array[Int]], mb:Array[Array[Int]])  
    {
       this(x,y,width,height)
       if (detailMesure(mr, mg, mb, x, y, width, height) < threshold) {
         this.color = averageColor(mr, mg, mb, x, y, width, height)
       }else{
         nodeList = new Array[PixelGrid](4)
         this.nodeList(0) = new PixelGrid( x, y,width/2, height/2,threshold,mr, mg, mb)
         this.nodeList(1) = new PixelGrid( x+width/2, y,width/2, height/2,threshold,mr, mg, mb)
         this.nodeList(2) = new PixelGrid( x, y+height/2,width/2, height/2,threshold,mr, mg, mb)
         this.nodeList(3) = new PixelGrid( x+width/2, y+height/2,width/2, height/2,threshold,mr, mg, mb)
       }
    } 
  /*
   * calculate squared region of a PixelGrid
   * 
   */
  def averageColor(mr:Array[Array[Int]], mg:Array[Array[Int]], mb:Array[Array[Int]], x:Int, y:Int, width:Int, height:Int ) : Colors = {
      var sum:Colors = new Colors(red= 0, green= 0, blue= 0)
      var area:Int = width * height
      for( i <- x until x+width) 
        { 
            for( j <- y until y+height) 
              { 

                  sum.red += mr(i)(j)
                  sum.green += mg(i)(j)
                  sum.blue += mb(i)(j)                 
              }  
        } 
                  sum.red /= area
                  sum.green /= area
                  sum.blue /= area
      return sum
   }
def detailMesure(mr:Array[Array[Int]], mg:Array[Array[Int]], mb:Array[Array[Int]], x:Int, y:Int, width:Int, height:Int ) : Int = {
      val avgColor:Colors = averageColor(mr, mg, mb, x, y, width, height)
      val red:Int = avgColor.red
      val green:Int = avgColor.green
      val blue:Int = avgColor.blue
      var colorSum:Int = 0
      for( i <- x until x+width) 
        { 
            for( j <- y until y+height) 
              { 

               colorSum =colorSum + (red-mr(i)(j)).abs+(green-mg(i)(j)).abs+(blue-mb(i)(j)).abs
                
              }  
        }
      return colorSum/(3*width*height)
   }
def  getColor(i:Int,j:Int) : Colors = 
{
   if(this.nodeList==null){
     
     return this.color
   }else if(i<this.x+this.width/2 ){
     if(j< this.y+this.height/2){
       return this.nodeList(0).getColor(i, j)
     }else{
       return this.nodeList(2).getColor(i, j)
     }
   }else{
      if(j< this.y+this.height/2){
       return this.nodeList(1).getColor(i, j)
     }else{
       return this.nodeList(3).getColor(i, j)
     }
   }
}
}

class QuadTree(root : PixelGrid) { }

class Colors (var red: Int, var green : Int, var blue: Int){ }

