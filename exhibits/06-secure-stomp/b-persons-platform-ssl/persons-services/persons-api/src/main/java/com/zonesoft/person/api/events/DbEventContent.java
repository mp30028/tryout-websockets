package com.zonesoft.person.api.events;

import java.util.Objects;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.zonesoft.person.api.utils.ToStringHelper;

public class DbEventContent<T> {
	
	private String eventTime;
	private String databaseName;
	private String collectionName;
	private String documentKey;
	private String operationType;
	private T body;
	
	public DbEventContent(ChangeStreamDocument<Document> d, T body) {
		BsonDocument extraElements = d.getExtraElements();
		this.eventTime = Objects.nonNull(extraElements) ? extraElements.get("timestamp").toString() : null;
		this.databaseName = d.getDatabaseName();
		this.collectionName = Objects.nonNull(extraElements) ? d.getExtraElements().get("collectionName").toString() : null;		
		ObjectId oid = d.getDocumentKey().get("_id").asObjectId().getValue();
		this.documentKey = oid.toHexString();
		this.operationType = d.getOperationType().getValue();
		this.body = body;
	}

	public String getEventTime() {
		return eventTime;
	}
	
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}
	
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	public String getCollectionName() {
		return collectionName;
	}
	
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	
	public String getDocumentKey() {
		return documentKey;
	}
	
	public void setDocumentKey(String documentKey) {
		this.documentKey = documentKey;
	}
	
	public String getOperationType() {
		return operationType;
	}
	
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	public T getBody() {
		return body;
	}
	
	public void setBody(T body) {
		this.body = body;
	}
	
	@Override
	public String toString(){
		ToStringHelper helper = new ToStringHelper();
		return helper.begin()
			.wrLn("eventTime", this.eventTime)
			.wrLn("databaseName", this.databaseName)
			.wrLn("collectionName", this.collectionName)
			.wrLn("documentKey", this.documentKey)
			.wrLn("operationType", this.operationType)
			.wr("body", this.body)
		.end().build();
	}	
}
