package ru.idealplm.utils.specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ru.idealplm.utils.specification.Specification.BlockContentType;
import ru.idealplm.utils.specification.Specification.BlockType;

public class Block {
	
	private String blockTitle;
	private ArrayList<BlockLine> blockLines;
	private ArrayList<String> blockLineUIDs;
	private Comparator<BlockLine> sortComparator;
	private Comparator<BlockLine> nonSortComparator;
	private BlockContentType blockContentType;
	private BlockType blockType;
	private int firstPosNo = 0;
	private int reserveLinesNum = 0;
	private int reservePosNum = 0;
	private int intervalPosNum = 0;
	private int renumerizableLines = 0;
	private boolean isRenumerizable = true;
	
	public Block(BlockContentType blockContentType, BlockType blockType, Comparator<BlockLine> sortComparator, Comparator<BlockLine> nonSortComparator, int reserveLinesNum) {
		this.blockContentType = blockContentType;
		this.blockType = blockType;
		this.blockTitle = Specification.blockTitles.get(blockContentType);
		this.sortComparator = sortComparator;
		this.nonSortComparator = nonSortComparator;
		this.reserveLinesNum = reserveLinesNum;
		blockLines = new ArrayList<BlockLine>();
		blockLineUIDs = new ArrayList<String>();
	}
	
	public void sort() {
		if(blockLines!=null){
			if(Specification.settings.getBooleanProperty("doRenumerize") || Specification.settings.getBooleanProperty("doUseReservePos")){
				Collections.sort(blockLines, sortComparator);
			} else {
				Collections.sort(blockLines, nonSortComparator);
			}
		}
	}
	
	public void setIsRenumerizable(boolean value){
		this.isRenumerizable = value;
	}
	
	public boolean isRenumerizable(){
		return isRenumerizable;
	}

	public String getBlockTitle() {
		return blockTitle;
	}
	
	public BlockContentType getBlockContentType(){
		return blockContentType;
	}

	public synchronized ArrayList<BlockLine> getListOfLines() {
		return blockLines;
	}
	
	public synchronized int size(){
		return blockLines.size();
	}
	
	public void setFirstPosNo(int no){
		this.firstPosNo = no;
	}
	
	public int getFirstPosNo(){
		return firstPosNo;
	}
	
	public int getReserveLinesNum(){
		return reserveLinesNum;
	}
	
	public void setReserveLinesNum(int reserveLinesNum){
		this.reserveLinesNum = reserveLinesNum;
	}
	
	public BlockType getBlockType(){
		return blockType;
	}
	
	public synchronized void addBlockLine(String uid, BlockLine blockLine){
		int pos = -1;
		if((pos = blockLineUIDs.indexOf(uid))!=-1){
			if(blockLine.isSubstitute!=blockLines.get(pos).isSubstitute){
				Specification.errorList.addError(new Error("ERROR", "Замена с идентификатором " + blockLine.attributes.getId() + " дублируется в составе сборки."));
			}
			updateBlockLine(blockLines.get(pos), blockLine);
			return;
		}
		if(!blockLine.isSubstitute) renumerizableLines++;
		blockLines.add(blockLine);
		blockLineUIDs.add(uid);
	}
	
	public void updateBlockLine(BlockLine target, BlockLine line){
		if(line.isSubstitute){
			return;
		}
		target.attributes.getRemark().insert(line.attributes.getRemark().getAll());
		if(target.attributes.getKits()!=null){
			target.attributes.getKits().addKits(line.attributes.getKits());
		}
		target.attributes.addQuantity(String.valueOf(line.attributes.getQuantity()));
		target.attributes.getZone().addZone(line.attributes.getZone());
		target.refBOMLines.addAll(line.getRefBOMLines());
		
		boolean targetPosIsEmpty = target.attributes.getPosition().isEmpty();
		boolean linePosIsEmpty = line.attributes.getPosition().isEmpty();
		boolean differentPos = !target.attributes.getPosition().equals(line.attributes.getPosition());
				
		if(differentPos && targetPosIsEmpty){
			target.attributes.setPosition(line.attributes.getPosition());
		} else if (differentPos && linePosIsEmpty){
			line.attributes.setPosition(target.attributes.getPosition());
		} else if (differentPos && !linePosIsEmpty && !targetPosIsEmpty){
			Specification.errorList.addError(new Error("ERROR", "Разные номера позиций у входимости с идентификатором " + line.attributes.getId()));
		}
	}
	
	public int getRenumerizableLinesCount(){
		return renumerizableLines;
	}

	public int getIntervalPosNum() {
		return intervalPosNum;
	}

	public void setIntervalPosNum(int intervalPosNum) {
		this.intervalPosNum = intervalPosNum;
	}

	public int getReservePosNum() {
		return reservePosNum;
	}

	public void setReservePosNum(int reservePosNum) {
		this.reservePosNum = reservePosNum;
	}

}
