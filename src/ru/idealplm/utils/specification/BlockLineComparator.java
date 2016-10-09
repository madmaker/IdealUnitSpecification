package ru.idealplm.utils.specification;

import java.util.Comparator;

import ru.idealplm.utils.specification.blockline.BlockLine;

public interface BlockLineComparator extends Comparator<BlockLine>{

	public int compare(BlockLine arg0, BlockLine arg1);

}
