package com.zonesoft.person.api.entities;

import java.util.Objects;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.model.changestream.ChangeStreamDocument;

public class DbEvent<T> {
	String timestamp;
	String databaseName;
	String collectionName;
	String documentKey;
	String operationType;
	T body;
	
	public DbEvent(ChangeStreamDocument<Document> d, T body) {
		BsonDocument extraElements = d.getExtraElements();
		this.timestamp = Objects.nonNull(extraElements) ? extraElements.get("timestamp").toString() : null;
		this.databaseName = d.getDatabaseName();
		this.collectionName = Objects.nonNull(extraElements) ? d.getExtraElements().get("collectionName").toString() : null;		
		ObjectId oid = d.getDocumentKey().get("_id").asObjectId().getValue();
		this.documentKey = oid.toHexString();
		this.operationType = d.getOperationType().getValue();
		this.body = body;
	}

	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
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
}
