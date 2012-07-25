package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.framework.util.FileUtil;
import com.framework.util.StringUtil;
import com.framework.util.WebUtil;
import com.istore.entity.StoreFile;
import com.istore.system.FileExistException;
import com.snda.storage.security.ProviderCredentials;
import com.snda.storage.security.SNDACredentials;
import com.snda.storage.service.CSService;
import com.snda.storage.service.impl.rest.httpclient.RestCSService;
import com.snda.storage.service.model.StorageObject;

public class MyTest { 
	private String bucket;
	private String accessKey;
	private String secretAccessKey;
	private String putFileName;	
	private String localfile; 
	private String localGetFileName; 
	private ProviderCredentials credentials;
	private CSService csService;
	@Rule  
    public ExternalResource resource= new ExternalResource() {  
        @Override  
        protected void before() throws Throwable {     
        	accessKey = "itjmkg6osxh038wko";
        	secretAccessKey = "NzIxODQ1MGUwNzJlMzJlZmIwMzIxZTQyYmExYWZhNzg=";
        	bucket = "qq911110yufeia"; 
        	//putFileName = "testFile";
        	putFileName = "aaa.txt";
        	localfile = "D:\\tool\\aaa.txt";
        	//localGetFileName = "E:\\Subclipse-1.2.4.zip";
        	localGetFileName = "E:\\a.txt";
			credentials = new SNDACredentials(accessKey,secretAccessKey); 
			csService = new RestCSService(credentials);	
        };   
    };
    
/*    @Test
    public void testBucketList() throws DocumentException {
    	StorageObject[] objects = csService.listObjects(bucket);
    	SAXReader reader = new SAXReader();
    	File file = new File("c:\\file.xml");
		Document doc = reader.read(file); 
		for (StorageObject object : objects) {
			List<?> selectList = doc.selectNodes("/Files/File[@key='"+ object.getKey() + "']" );
			if (selectList.size() == 0) {
				System.out.println("文件没找到:" + object.getKey());
			}
		}
		System.out.println(objects.length);
    }*/
/*    @Test
    public void testWriteXML() throws DocumentException, IOException {
    	SAXReader reader = new SAXReader();
    	File file = new File("c:\\file.xml");
		Document doc = reader.read(file);
		Element root = doc.getRootElement();
		int count = 0;
		List<StoreFile> list = new ArrayList<StoreFile>();
		for (Iterator<?> i = root.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			String name = element.element("Name").getText();
			String catalogKey = element.element("CatalogKey").getText();
			String key = element.element("Key").getText();
			String size = "";
			if (element.element("Size") != null) {
				size = element.element("Size").getText();
			} 
			StoreFile storeFile = new StoreFile();
			storeFile.setCatalogKey(catalogKey);
			storeFile.setKey(key);
			storeFile.setName(name);
			storeFile.setSize(size);  
			StorageObject headObject = csService.headObject(bucket , storeFile.getKey());
			if(headObject == null) {
				System.out.println(storeFile.getName() + ":无法找到！！" +  storeFile.getKey());
			} else {
				storeFile.setSize(FileUtil.formetFileSize(headObject.getContentLength()));
				System.out.println(storeFile.getName() + ":" 
						+ storeFile.getSize() + "   " + storeFile.getKey());		
			}
			list.add(storeFile);
			count++;
		} 
		System.out.println("文件数量为"+count);  
		for (StoreFile storeFile : list) {
	        List<?> selectList = doc.selectNodes("/Files/File[@key='"+ storeFile.getKey() + "']" );
			Iterator<?> iter = selectList.iterator(); 
			while (iter.hasNext()) {
				Element el = (Element) iter.next();  
				if (!StringUtil.isEmpty(storeFile.getSize())) {
					el.addElement("Size").addText(storeFile.getSize()); 			
				} 
			}
		}
        FileUtil.writeXML(doc, getStoreFile());
    }*/
    
	public File getStoreFile() { 
		File file = new File("c:\\file.xml");
        if (!file.exists()) {    //没有找到用户对应的配置文件　用户不存在
        	throw new FileExistException();
        }
        return file;
	}
    
	/*@Test
	public void test() {

		try {

			InetAddress inetHost = InetAddress.getByName("storage.grandcloud.cn");
			String hostName = inetHost.getHostName();
			System.out.println("The host name was: " + hostName);
			System.out.println("The hosts IP address is: "
					+ inetHost.getHostAddress());

		} catch (UnknownHostException ex) {

			System.out.println("Unrecognized host");
		} 
		assertTrue(true);
	} */
	
/*	@Test
	public void testGetBucketOfFiles() throws NoSuchAlgorithmException, IOException {
		CSObject[] objects = (CSObject[]) csService.listObjects(bucket);
		assertEquals(objects.length, 4);
	}*/
	
/*	@Test
	public void testPutObject() throws NoSuchAlgorithmException, IOException {
		StorageObject object = new  CSObject( new File(localfile));
		csService.putObject( bucket, object);
		assertTrue(true);
	}
	
	@Test
	public void testDownloadObject() throws Exception {
		StorageObject getObject = csService.getObject( bucket, putFileName);
		
		InputStream urlInputStream = getObject.getDataInputStream();
	    FileOutputStream fileOutputStream = new FileOutputStream(localGetFileName);
	    com.google.common.io.ByteStreams.copy(urlInputStream,
	    		fileOutputStream);
	    fileOutputStream.close();
	    urlInputStream.close();
		assertTrue(true);		
	}
	
	@Test
	public void testDeleteObject() {
		csService.deleteObject( bucket, putFileName);
		assertTrue(true);
	}*/
}
