package com.tairan.cloud.service.mongodb;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Demo1 {

	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient("172.30.249.17", 27017);
		MongoDatabase mongoDatabase = mongoClient.getDatabase("mydb");
		/**
		 * 创建集合
		 */
		// mongoDatabase.createCollection("javatest");
		/**
		 * 选择集合
		 */
		MongoCollection<Document> collection = mongoDatabase.getCollection("javatest");
		/**
		 * 向集合中插入一条Document
		 */
		// Document document = new Document("title",
		// "MongoDB").append("description", "database");
		// collection.insertOne(document);
		/**
		 * 检索所有文档
		 */
		// FindIterable<Document> findIterable = collection.find();
		// for(Document doc:findIterable){
		// System.out.println(doc);
		// }
		/**
		 * 更新文档 $set 只更改titile属性 不更改其他属性
		 */
		collection.updateMany(Filters.eq("title", "MongoDB"), new Document("$set", new Document("title", "MongoDB1")));

		FindIterable<Document> findIterable = collection.find();
		for (Document doc : findIterable) {
			System.out.println(doc);
		}

		/**
		 * 删除符合条件的第一个文档
		 */
		collection.deleteOne(Filters.eq("title", "MongoDB"));
		/**
		 * 删除所有符合条件的文档
		 */
		collection.deleteMany(Filters.eq("title", "MongoDB"));
	}

}
