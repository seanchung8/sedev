package hortonworks.hdp.refapp.ecm.web;

import hortonworks.hdp.refapp.ecm.service.api.DocumentClass;
import hortonworks.hdp.refapp.ecm.service.api.DocumentMetaData;
import hortonworks.hdp.refapp.ecm.service.api.DocumentServiceAPI;
import hortonworks.hdp.refapp.ecm.service.api.DocumentUpdateRequest;
import hortonworks.hdp.refapp.ecm.service.core.docstore.dto.DocumentDetails;
import hortonworks.hdp.refapp.ecm.service.core.indexstore.dto.DocumentSearchRequest;
import hortonworks.hdp.refapp.ecm.service.core.indexstore.dto.QueryResult;
import hortonworks.hdp.refapp.ecm.util.MimeUtils;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DocumentManagementController {

	private static final Logger LOG = Logger.getLogger(DocumentManagementController.class);
	
	@Autowired
	DocumentServiceAPI docServiceAPI;
	
	
    @RequestMapping(value="/ecm/search", method= RequestMethod.GET)
    public String renderSearch () {
    	return "ecm/index";
    }
    
    @RequestMapping(value="/ecm/executeSearch", method= RequestMethod.GET)
    public @ResponseBody QueryResult search(@RequestParam(value="query", required=true) String searchValue, 
    										@RequestParam(value= "filter", required=false) String filter, 
    										@RequestParam(value="start", required=false) Integer start, 
    										@RequestParam(value="facet.field", required=true) String facetField, 
    										@RequestParam(value="highlight.field", required=true) String highlightField) {
    	
    	LOG.info("Querying:" + searchValue);
    	LOG.info("Filter: " + filter);
    	LOG.info("Start: " + start);
    	LOG.info("facetField: " + facetField);
    	LOG.info("hightField: " + highlightField);
    	
    	DocumentSearchRequest request = new DocumentSearchRequest();
    	request.setSearchValue(searchValue);
    	request.setStart(start == null ? 0 : start);
    	request.setFacetField(facetField);
    	request.setHighlightField(highlightField);
    	
    	try {
			return docServiceAPI.searchDocuments(request);
		} catch (Exception e) {
			LOG.error("Error searching", e);
			return null;
		}

    }	
    
	@RequestMapping(value="/ecm/upload", method=RequestMethod.POST)
	public @ResponseBody String uploadDocumentAndMetadata(@RequestParam String documentName, String documentClass, String customerName, MultipartFile file ) throws Exception {
		
		if(LOG.isInfoEnabled()) {
			LOG.info("Uploading Doc[" + documentName + "]");
			LOG.info("DocumentName: " + documentName);
			LOG.info("DocumentClass: " + documentClass);
			LOG.info("Customer Name: " + customerName);
			LOG.info("File Name from MultiPart: " + file.getName());
			LOG.info("File Type from MultiPart: " + file.getContentType());
			LOG.info("File Bytes from MultiPart: " + file.getBytes());
		}
		String extension = MimeUtils.getExtension(file.getContentType());
		DocumentMetaData docMetadata = new DocumentMetaData(documentName, file.getContentType(), DocumentClass.valueOf(documentClass), customerName, extension);
		DocumentUpdateRequest docUpdateRequest = new DocumentUpdateRequest(null, file.getBytes(), docMetadata);
		docServiceAPI.addDocument(docUpdateRequest);
		return "Uploaded Successfull";
	}    
    
//	private String getExtension(String contentType) {
//		String extension = null;
//		if(contentType.equals("application/pdf")) {
//			extension = ".pdf";
//		} else if(contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
//			extension = ".docx";
//		} else {
//			String errMsg = "Unrecognized Content-type["+ contentType + "] to get a corresponding extension";
//			LOG.error(errMsg);
//			throw new RuntimeException(errMsg);
//		}
//		
//		return extension;
//		
//	}

	@RequestMapping("/ecm/download")
	public void downloadDocument( @RequestParam String docId,HttpServletResponse response) throws Exception {
		
		LOG.info("Downloading Doc with docId["+docId + "]");
		DocumentDetails docDetails = docServiceAPI.getDocument(docId);
		byte[] reportData = docDetails.getDocContent();
		LOG.info("Finished Downloading Doc with docId["+docId + "]:" + reportData);
		
		String contentType = docDetails.getDocProperties().getMimeType();
		response.setContentType(contentType);
		String contentDispositionValue = createContentDispositionValue(docDetails.getDocProperties().getName(), docDetails.getDocProperties().getExtension());
		response.setHeader("content-disposition", contentDispositionValue);
		response.getOutputStream().write(reportData);
	}
	
	
	/*
	 * Shoudl be in the following format: attachment;filename=results.pdf
	 */
	private String createContentDispositionValue(String name, String extension) {
		String dispositionValue = "attachment;filename="+name+extension;
		LOG.info("Disposotion Value is: " + dispositionValue);
		return dispositionValue;
	}


	
}
