package ru.idealplm.utils.specification;

import java.util.LinkedList;

import ru.idealplm.utils.specification.Specification.BlockContentType;
import ru.idealplm.utils.specification.Specification.BlockType;

public class BlockList extends LinkedList<Block>{
	
	private static final long serialVersionUID = 6092917094207891433L;
	private Object lock = new Object();

	public void addBlock(Block block){
		synchronized(lock) {
			this.addLast(block);
		}
	}
	
	public Block getBlock(BlockContentType blockContentType, BlockType blockType){
		synchronized(lock) {
			for(Block block : this){
				if(block.blockContentType==blockContentType && block.blockType==blockType) return block;
			}
			return null;
		}
	}
	
	public void removeBlock(int i){
		synchronized(lock) {
			this.remove(i);
		}
	}
	
}
