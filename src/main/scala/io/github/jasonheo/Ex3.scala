package io.github.jasonheo.Ex3

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer, JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}

case class Company(name: String, address:String)

@JsonDeserialize(using = classOf[PersonDeserializer])
case class Person(name: String, companies: Option[List[Company]])

object Ex3 {
  def main(args: Array[String]): Unit = {
    val objectMapper: ObjectMapper = new ObjectMapper() with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)

    val json = """
    {
      "name": "Kim",
      "companies": [
        {
          "name": "my-company",
          "address": "Seoul"
        },
        {
          "name": "your-company",
          "address": "Busan"
        }
      ]
    }
    """

    val person: Person = objectMapper.readValue(json, classOf[Person])

    println(person)
    // 출력 결과: Person(Kim,Some(List(Company(my-company,Seoul), Company(your-company,Busan))))
  }
}

class PersonDeserializer extends JsonDeserializer[Person] {
  override def deserialize(jsonParser: JsonParser,
                           ctxt: DeserializationContext): Person = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)

    val companiesNode: JsonNode = node.get("companies")

    if (companiesNode.isArray) {
      import scala.collection.JavaConverters._

      // 핵심 포인트: asScala를 이용하여 scala 객체로 만든 뒤 map을 사용하는 부분
      val companies: List[Company] = companiesNode.asScala.map {
        companyNode: JsonNode => {
          Company(
            name = companyNode.get("name").textValue(),
            address = companyNode.get("address").textValue()
          )
        }
      }.toList

      Person(
        name = node.get("name").textValue(),
        companies = Some(companies)
      )
    }
    else { // "companies"에 값이 없는 경우
      Person(
        node.get("name").textValue(),
        None
      )
    }
  }
}