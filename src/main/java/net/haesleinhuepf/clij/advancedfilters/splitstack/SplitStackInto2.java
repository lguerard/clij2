package net.haesleinhuepf.clij.advancedfilters.splitstack;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_splitStackInto2")
public class SplitStackInto2 extends AbstractSplitStack {

    @Override
    public String getParameterHelpText() {
        return getParameterHelpText(2);
    }


}