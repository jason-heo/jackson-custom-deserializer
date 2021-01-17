package io.github.jasonheo.Ex2

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer, JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}

@JsonDeserialize(using = classOf[CompanyDeserializer])
case class Company(name: String, address:String)

case class Person(name: String, company: Option[Company])

object Ex2 {
  def main(args: Array[String]): Unit = {
    val objectMapper: ObjectMapper = new ObjectMapper() with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)

    val json = """
    {
      "name": "Kim",
      "company": {}
    }
    """

    val person: Person = objectMapper.readValue(json, classOf[Person])

    println(person)
    // 출력 결과: Person(Kim,None)
  }
}

class CompanyDeserializer extends JsonDeserializer[Company] {
  override def deserialize(jsonParser: JsonParser,
                           ctxt: DeserializationContext): Company = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)

    // 참고: https://stackoverflow.com/a/63030594/2930152
    // 위 문서에서는 if에 `node.asText().isEmpty()` 조건도 있었으나 제거했음
    if (node.isNull || node.size == 0) {
      null
    }
    else {
      Company(
        name = node.get("name").textValue(),
        address = node.get("address").textValue()
      )
    }
  }
}