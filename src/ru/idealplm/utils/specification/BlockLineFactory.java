package ru.idealplm.utils.specification;

import com.teamcenter.rac.kernel.TCComponentBOMLine;

public abstract class BlockLineFactory {
	
	public abstract BlockLine newBlockLine(TCComponentBOMLine bomLine);

}
