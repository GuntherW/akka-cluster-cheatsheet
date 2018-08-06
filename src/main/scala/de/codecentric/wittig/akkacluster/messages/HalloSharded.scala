package de.codecentric.wittig.akkacluster.messages

/**
  * "Hallo" gewrappt fürs Sharding, damit noch ein eindeutiger Identifier pro Nachricht mit angegeben werden kann.
  * Natürlich sollte besser eine UUID oder ein Int genommen werden. Einfach daß, was das Entity eindeutig identifiziert.
  */
case class HalloSharded(name: String, hallo: Hallo)
