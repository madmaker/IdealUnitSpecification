package ru.idealplm.utils.specification;

import com.teamcenter.rac.kernel.TCComponentBOMLine;

import ru.idealplm.utils.specification.blockline.BlockLine;

public abstract class BlockLineFactory {
	
	public abstract BlockLine newBlockLine(TCComponentBOMLine bomLine);

}
