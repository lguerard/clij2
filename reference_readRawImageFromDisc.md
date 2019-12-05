## readRawImageFromDisc
![Image](images/mini_clijx_logo.png)

Reads a raw file from disc and pushes it immediately to the GPU.

### Usage in ImageJ macro
```
Ext.CLIJx_readRawImageFromDisc(Image destination, String filename, Number width, Number height, Number depth, Number bitsPerPixel);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
```

```
// Execute operation on GPU
clijx.readRawImageFromDisc(clij, arg1, arg2);
```

```
//show result

// cleanup memory on GPU
arg1.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [halfCylinderProjection.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/halfCylinderProjection.py)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)