package ru.idealplm.utils.specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.teamcenter.rac.kernel.TCComponentBOMLine;

import ru.idealplm.utils.specification.Specification.BlockContentType;
import ru.idealplm.utils.specification.Specification.BlockType;
import ru.idealplm.utils.specification.Specification.FormField;
import ru.idealplm.utils.specification.util.LineUtil;

public class BlockLine {
	
	public class BlockLineAttributes{
		
		private BLFormat format = null;
		private BLZone zone = null;
		private BLRemark remark = null;
		private BLKits kits = null;
		private String position = "";
		private String id = "";
		private ArrayList<String> name = null;
		private double quantity = 0;
		
		public BlockLineAttributes(){
			this.format = new BLFormat("");
			this.zone = new BLZone("");
			this.remark = new BLRemark("");
			this.name = new ArrayList<String>();			
		}
		
		public void setFormat(String format) {
			this.format = new BLFormat(format);
		}
		
		public void setZone(String zone) {
			if(this.zone==null){
				this.zone = new BLZone(zone);
			} else {
				this.zone.addZone(zone);
			}
		}
		
		public void setRemark(String remark){
			if(this.remark==null) this.remark = new BLRemark();
			this.remark.insert(remark);
		}
		
		public void createKits(){
			if(this.kits==null)	this.kits = new BLKits();
		}
		
		public void addKit(String id, String name, double qty){
			if(kits==null) this.kits = new BLKits();
			this.kits.addKit(id, name, qty);
		}
		
		public void addKit(BLKits kit){
			this.kits.addKits(kit);
		}
		
		public BLKits getKits(){
			return this.kits;
		}
		
		public void setPosition(String position) {
			this.position = position;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public void setName(String name) {
			this.name = LineUtil.getFittedLines(name, SpecificationSettings.columnLengths.get(FormField.NAME));
			if(this.name.size() > lineHeight) lineHeight = this.name.size();
		}
		
		public void setQuantity(String quantity) {
			this.quantity = "".equals(quantity) ? 1 : Double.parseDouble(quantity);
		}
		
		public void addQuantity(String quantity){
			this.quantity = this.quantity + ("".equals(quantity) ? 1 : Double.parseDouble(quantity));
		}
		
		public BLFormat getFormat() {
			return format;
		}
		public BLZone getZone() {
			return zone;
		}
		public String getPosition() {
			return position;
		}
		public String getId() {
			return id;
		}
		public ArrayList<String> getName() {
			return name;
		}
		public double getQuantity() {
			return quantity;
		}
		public BLRemark getRemark() {
			if(this.remark==null) remark = new BLRemark();
			return remark;
		}
		
		public String getStringValueFromField(FormField field) {
			switch(field) {
				case FORMAT: return format.toString();
				case ZONE: return zone.toString();
				case POSITION: return position;
				case ID: return id;
				case NAME: return Arrays.toString(name.toArray());
				case QUANTITY: return quantity==0?" ":(quantity%1==0) ? Integer.toString((int)quantity) : Double.toString(quantity);
				case REMARK: return remark.toString();
			}
			return "";
		}
	}
	
	public BlockLineAttributes attributes = null;
	public BlockContentType blockContentType = null;
	public BlockType blockType = null;
	public boolean isSubstitute = false;
	public boolean isRenumerizable = true;
	public int lineHeight = 1;
	public String uid = "";
	
	public ArrayList<BlockLine> substituteBlockLines = null;
	public ArrayList<TCComponentBOMLine> refBOMLines = null;
	public ArrayList<BlockLine> attachedLines = new ArrayList<BlockLine>();
	
	private HashMap<String, String> props;
	private BlockLineHandler blockLineHandler;
	
	public BlockLine() {
		attributes = new BlockLineAttributes();
	}
	
	public BlockLine(BlockLineHandler blockLineHandler) {
		this();
		this.blockLineHandler = blockLineHandler;
	}
	
	public BlockLine(BlockLineHandler blockLineHandler, boolean isSubstitute) {
		this(blockLineHandler);
		this.isSubstitute = isSubstitute;
	}
	
	public void addSubstituteBlockLine(BlockLine blockLine){
		if(this.substituteBlockLines==null) substituteBlockLines = new ArrayList<BlockLine>();
		substituteBlockLines.add(blockLine);
	}
	
	public void addRefBOMLine(TCComponentBOMLine bomLine){
		if(this.refBOMLines==null) refBOMLines = new ArrayList<TCComponentBOMLine>();
		refBOMLines.add(bomLine);
	}
	
	public ArrayList<BlockLine> getSubstituteBlockLines(){
		return substituteBlockLines;
	}
	
	public ArrayList<TCComponentBOMLine> getRefBOMLines(){
		return refBOMLines;
	}
	
	public ArrayList<BlockLine> getAttachedLines(){
		return attachedLines;
	}
	public void addProperty(String key, String value){
		if(props==null) props = new HashMap<String,String>();
		props.put(key, value);
	}
	public String getProperty(String key){
		return (props!=null)?props.get(key):null;
	}
	
	public BlockLine build(){
		if(attachedLines!=null){
			for(BlockLine line:attachedLines){
				line.build();
			}
		}
		if(blockLineHandler!=null) blockLineHandler.prepareBlockLine(this);
		return this;
	}
}
