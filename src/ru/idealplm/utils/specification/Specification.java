package ru.idealplm.utils.specification;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ru.idealplm.utils.specification.methods.AttachMethod;
import ru.idealplm.utils.specification.methods.DataReaderMethod;
import ru.idealplm.utils.specification.methods.PrepareMethod;
import ru.idealplm.utils.specification.methods.ReportBuilderMethod;
import ru.idealplm.utils.specification.methods.ValidateMethod;
import ru.idealplm.utils.specification.methods.XmlBuilderMethod;

import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCSession;

public class Specification {
	
	public static final String CLIENT_ID = UUID.randomUUID().toString();
	private static Specification instance = null;
	private static final Object lock = new Object();
	private static boolean isInitialized = false;
	
	public static enum FormField {FORMAT, ZONE, POSITION, ID, NAME, QUANTITY, REMARK};
	public static enum BlockContentType {DOCS, COMPLEXES, ASSEMBLIES, DETAILS, STANDARDS, OTHERS, MATERIALS, KITS};
	public static enum BlockType {DEFAULT, ME};
	public static final SpecificationSettings settings = SpecificationSettings.getInstance();
	
	private BlockList blockList;
	private File xmlFile = null;
	private File reportFile = null;
	
	private ValidateMethod validateMethod;
	private DataReaderMethod dataReaderMethod;
	private PrepareMethod prepareMethod;
	private XmlBuilderMethod xmlBuilderMethod;
	private ReportBuilderMethod reportBuilderMethod;
	private AttachMethod attachMethod;
	
	public static ErrorList errorList;
	
	private TCComponentBOMLine topBOMLine;
	private TCComponentItemRevision specificationItemRevision = null;
	public static TCSession session = (TCSession) AIFUtility.getCurrentApplication().getSession();
	public static TCPreferenceService preferenceService = session.getPreferenceService();
	
	public static Map<BlockContentType, String> blockTitles = new HashMap<BlockContentType, String>();
	static {
		blockTitles.put(BlockContentType.DOCS, "Документация");
		blockTitles.put(BlockContentType.COMPLEXES, "Комплексы");
		blockTitles.put(BlockContentType.ASSEMBLIES, "Сборочные единицы");
		blockTitles.put(BlockContentType.DETAILS, "Детали");
		blockTitles.put(BlockContentType.STANDARDS, "Стандартные изделия");
		blockTitles.put(BlockContentType.OTHERS, "Прочие изделия");
		blockTitles.put(BlockContentType.MATERIALS, "Материалы");
		blockTitles.put(BlockContentType.KITS, "Комплекты");
	}
	
    public static Specification getInstance() {
    	if (!isInitialized) {
			synchronized (lock) {
				if (instance == null) {
					instance = new Specification();
					isInitialized = true;
				}
			}
		}
		return instance;
    }
    
    public void init(TCComponentBOMLine topBOMLine, ValidateMethod validateMethod, DataReaderMethod dataReaderMethod, PrepareMethod prepareMethod, XmlBuilderMethod xmlBuilderMethod, ReportBuilderMethod reportBuilderMethod, AttachMethod attachMethod){
    	this.topBOMLine = topBOMLine;
    	this.validateMethod = validateMethod;
    	this.dataReaderMethod = dataReaderMethod;
    	this.prepareMethod = prepareMethod;
    	this.xmlBuilderMethod = xmlBuilderMethod;
    	this.reportBuilderMethod = reportBuilderMethod;
    	this.attachMethod = attachMethod;
    	errorList = new ErrorList();
    	blockList = new BlockList();
    }
	
	public void setSpecificationItemRevision(TCComponentItemRevision specIR){
		this.specificationItemRevision = specIR;
	}
	
	public TCComponentItemRevision getSpecificationItemRevision(){
		return this.specificationItemRevision;
	}
	
	public boolean validate(){
		return validateMethod.validate();
	}
	
	public void prepareBlocks(){
		prepareMethod.prepareBlocks();
	}
	
	public void readBOMData(){
		dataReaderMethod.readBOMData();
	}
	
	public void makeXmlFile(){
		xmlFile = xmlBuilderMethod.makeXmlFile();
	}
	
	public File getXmlFile(){
		return xmlFile;
	}
	
	public void makeReportFile(){
		reportFile = reportBuilderMethod.makeReportFile();
	}
	
	public File getReportFile(){
		return reportFile;
	}
	
	public void putInTeamcenter(){
		attachMethod.putInTeamcenter();
	}
	
	public TCComponentBOMLine getTopBOMLine(){
		return topBOMLine;
	}
	
	public BlockList getBlockList(){
		return blockList;
	}
	
	public void setBlockList(BlockList blockList){
		this.blockList = blockList;
	}
	
	public ErrorList getErrorList(){
		return errorList;
	}
	
	public void cleanUp(){
		blockList.clear();
		blockList = null;
		settings.cleanUp();
	}
}
