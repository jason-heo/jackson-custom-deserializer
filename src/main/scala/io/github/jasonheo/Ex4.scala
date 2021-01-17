package io.github.jasonheo.Ex4

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer, JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}

case class Company(name: String, address:String)

@JsonDeserialize(using = classOf[PersonDeserializer])
case class Person(name: String, company: Option[Company])

object Ex4 {
  def main(args: Array[String]): Unit = {
    val objectMapper: ObjectMapper = new ObjectMapper() with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)

    val json = """
    {
      "name": "Kim",
      "company": {
        "name": "my-company",
        "address": "Seoul"
      }
    }
    """

    val person: Person = objectMapper.readValue(json, classOf[Person])

    println(person)
    // 출력 결과: Person(Kim,Some(Company(my-company,Seoul)))
  }
}

class PersonDeserializer extends JsonDeserializer[Person] {
  override def deserialize(jsonParser: JsonParser,
                           ctxt: DeserializationContext): Person = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)

    val company: Company = {
      // objectMapper를 이용하여 Company를 생성한다
      val objectMapper: ObjectMapper = new ObjectMapper() with ScalaObjectMapper
      objectMapper.registerModule(DefaultScalaModule)

      objectMapper.readValue(node.get("company").toString, classOf[Company])
    }

    Person(
      name = node.get("name").textValue(),
      company = Some(company)
    )
  }
}