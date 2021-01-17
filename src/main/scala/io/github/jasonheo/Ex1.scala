package io.github.jasonheo

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer, JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}

@JsonDeserialize(using = classOf[PersonDeserializer])
case class Person(id: Int, name: String)

object Ex1 {
  def main(args: Array[String]): Unit = {
    val objectMapper: ObjectMapper = new ObjectMapper() with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)

    val json = """
    {
      "id": 1,
      "name": "Kim"
    }
    """

    val person: Person = objectMapper.readValue(json, classOf[Person])

    println(person)
    // 출력 결과: Person(1,Kim)
  }
}

class PersonDeserializer extends JsonDeserializer[Person] {
  override def deserialize(jsonParser: JsonParser,
                           ctxt: DeserializationContext): Person = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)

    Person(
      id = node.get("id").intValue(),
      name = node.get("name").textValue()
    )
  }
}