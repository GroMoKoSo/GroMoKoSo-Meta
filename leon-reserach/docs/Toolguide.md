# Tool‑Modul – Entwicklerhandbuch

*Generiert am 10.07.2025 16:36:49*

## Überblick

Dieses Handbuch dokumentiert das **Spring AI Tool‑Modul**, das sich im Archiv `tool.zip` befindet. Das Modul stellt eine kohärente Menge von Schnittstellen, Annotationen und Hilfsklassen bereit, mit denen *von einem KI‑Modell aufrufbare Tools* in Spring‑Anwendungen deklariert, aufgelöst, ausgeführt und beobachtet werden können.

Der typische Datenfluss lautet *Definition → Auflösung → Ausführung*:

1. **Definition** – Sie beschreiben, was ein Tool tut (`ToolDefinition`) und annotieren eine Methode, Funktion oder einen Bean.
2. **Auflösung** – Ein `ToolCallbackResolver` findet eine `ToolCallback`‑Implementierung, die das Tool ausführen kann.
3. **Ausführung** – Das Callback führt die Business‑Logik aus, konvertiert das Ergebnis über einen `ToolCallResultConverter` in eine String‑Antwort und verarbeitet Fehler über einen `ToolExecutionExceptionProcessor`.

```
┌────────────┐      ┌───────────────────┐      ┌───────────────────┐
│ Definition │──▶  │  CallbackResolver │──▶  │   ToolCallback     │
└────────────┘      └───────────────────┘      └───────────────────┘
                                       ▲            │ führt aus     
                                       │            ▼               
                              Observation & Metriken (observation)  
```

## Package‑Übersicht

* **`org.springframework.ai.tool`** – 3 öffentliche Typen
* **`org.springframework.ai.tool.annotation`** – 2 öffentliche Typen
* **`org.springframework.ai.tool.definition`** – 2 öffentliche Typen
* **`org.springframework.ai.tool.execution`** – 5 öffentliche Typen
* **`org.springframework.ai.tool.function`** – 1 öffentliche Typen
* **`org.springframework.ai.tool.metadata`** – 2 öffentliche Typen
* **`org.springframework.ai.tool.method`** – 2 öffentliche Typen
* **`org.springframework.ai.tool.observation`** – 5 öffentliche Typen
* **`org.springframework.ai.tool.resolution`** – 5 öffentliche Typen
* **`org.springframework.ai.tool.support`** – 2 öffentliche Typen

## Öffentliche Typen pro Package

### org.springframework.ai.tool

* **StaticToolCallbackProvider** (Klasse) – A simple implementation of {@link ToolCallbackProvider} that maintains a static array
of {@link ToolCallback} objects. This provider is immutable after construction and
provides a straightforward way to supply a fixed set of tool callbacks to AI models.

<p>
This implementation is thread‑safe because it maintains an *immutable* array of callbacks: the array is created and populated during object construction **and cannot be modified afterwards**. Consequently, multiple threads can invoke the component concurrently without the risk of another thread mutating the callback list.
* **ToolCallback** (Interface) – Represents a tool whose execution can be triggered by an AI model.
* **ToolCallbackProvider** (Interface) – Provides {@link ToolCallback} instances for tools defined in different sources.

### org.springframework.ai.tool.annotation

* **Tool** (Interface) – Marks a method as a tool in Spring AI.
* **ToolParam** (Interface) – Marks a tool argument.

### org.springframework.ai.tool.definition

* **DefaultToolDefinition** (Record) – Default implementation of {@link ToolDefinition}.
* **ToolDefinition** (Interface) – Definition used by the AI model to determine when and how to call the tool.

### org.springframework.ai.tool.execution

* **DefaultToolCallResultConverter** (Klasse) – A default implementation of {@link ToolCallResultConverter}.
* **DefaultToolExecutionExceptionProcessor** (Klasse) – Default implementation of {@link ToolExecutionExceptionProcessor}. Can be configured
with an allowlist of exceptions that will be unwrapped from the
{@link ToolExecutionException} and rethrown as is.
* **ToolExecutionException** (Klasse) – An exception thrown when a tool execution fails.
* **to** (Interface) – A functional interface to convert tool call results to a String that can be sent back
to the AI model.
* **to** (Interface) – A functional interface to process a {@link ToolExecutionException} by either converting
the error message to a String that can be sent back to the AI model or throwing an
exception to be handled by the caller.

### org.springframework.ai.tool.function

* **FunctionToolCallback** (Klasse) – A {@link ToolCallback} implementation to invoke functions as tools.

### org.springframework.ai.tool.metadata

* **DefaultToolMetadata** (Record) – Default implementation of {@link ToolMetadata}.
* **ToolMetadata** (Interface) – Metadata about a tool specification and execution.

### org.springframework.ai.tool.method

* **MethodToolCallback** (Klasse) – A {@link ToolCallback} implementation to invoke methods as tools.
* **MethodToolCallbackProvider** (Klasse) – A {@link ToolCallbackProvider} that builds {@link ToolCallback} instances from
{@link Tool}-annotated methods.

### org.springframework.ai.tool.observation

* **DefaultToolCallingObservationConvention** (Klasse) – Default conventions to populate observations for tool calling operations.
* **ToolCallingContentObservationFilter** (Klasse) – An {@link ObservationFilter} to include the tool call content (input/output) in the
observation.
* **ToolCallingObservationContext** (Klasse) – Context used to store data for tool calling observations.
* **ToolCallingObservationConvention** (Interface) – Interface for an {@link ObservationConvention} for tool calling observations.
* **ToolCallingObservationDocumentation** (Enum) – Tool calling observation documentation.

### org.springframework.ai.tool.resolution

* **DelegatingToolCallbackResolver** (Klasse) – A {@link ToolCallbackResolver} that delegates to a list of {@link ToolCallbackResolver}
instances.
* **SpringBeanToolCallbackResolver** (Klasse) – A Spring {@link ApplicationContext}-based implementation that provides a way to
retrieve a bean from the Spring context and wrap it into a {@link ToolCallback}.
* **StaticToolCallbackResolver** (Klasse) – A {@link ToolCallbackResolver} that resolves tool callbacks from a static registry.
* **ToolCallbackResolver** (Interface) – A resolver for {@link ToolCallback} instances.
* **that** (Klasse) – A utility class that provides methods for resolving types and classes related to
functions.

### org.springframework.ai.tool.support

* **ToolUtils** (Klasse) – Miscellaneous tool utility methods. Mainly for internal use within the framework.
* **for** (Klasse) – Utility class for creating {@link ToolDefinition} builders and instances from Java
{@link Method} objects.
<p>
This class provides static methods to facilitate the construction of
{@link ToolDefinition} objects by extracting relevant metadata from Java reflection
{@link Method} instances.
</p>

## Wichtige Abstraktionen

### ToolCallback
Zentrales Interface für ein ausführbares Tool. Implementierungen erhalten die **Tool‑Eingabe** als JSON‑String und liefern einen menschenlesbaren String, der an das KI‑Modell zurückgegeben wird.
```java
public interface ToolCallback {
    ToolDefinition getToolDefinition();
    default ToolMetadata getToolMetadata() { ... }
    String call(String toolInput);
    default String call(String toolInput, @Nullable ToolContext ctx) { ... }
}
```

### ToolDefinition & DefaultToolDefinition
`ToolDefinition` ist ein Java 17‑Record, der **Name**, **Beschreibung** und **Input‑Schema** eines Tools enthält. Der Builder `DefaultToolDefinition.builder()` bietet eine flüssige API und leitet eine lesbare Beschreibung ab, falls keine angegeben wird.

### Annotationen: @Tool & @ToolParam
* `@Tool` markiert eine Spring‑Bean‑Methode als aufrufbares Tool. Annotation‑Attribute ermöglichen das Festlegen von Name, Beschreibung und Schema; andernfalls werden sinnvolle Standardwerte abgeleitet.
* `@ToolParam` kennzeichnet einen Methodenparameter, der dem KI‑Modell exponiert wird. Die Annotation wird zur Laufzeit in einer `ToolDefinition` erfasst.

### Auflösungsstrategien
* **StaticToolCallbackResolver** – kapselt ein explizites Array bzw. eine Collection von Callbacks.
* **SpringBeanToolCallbackResolver** – sucht im Spring‑Context nach Beans, die `ToolCallback` implementieren.
* **DelegatingToolCallbackResolver** – Komposit, das mehrere Resolver der Reihe nach ausprobiert.
* **TypeResolverHelper** – Hilfsklasse, die mittels Reflection Klassen‑ und Methodensignaturen abgleicht.

### Hilfsklassen zur Ausführung
* **ToolCallResultConverter** + **DefaultToolCallResultConverter** – konvertieren beliebige Java‑Objekte (POJOs, Collections) in ein vom KI‑Modell erwartetes Textformat.
* **ToolExecutionException** – Basisklasse für Tool‑Fehler.
* **ToolExecutionExceptionProcessor** + **DefaultToolExecutionExceptionProcessor** – mappen Exceptions auf menschenlesbare Meldungen.

### Observation
Pakete unter `observation` integrieren Micrometers Observation‑API, sodass jeder Tool‑Aufruf mit Standard‑Metriken und Tracing‑Spans wie `tool.call` versehen wird.

## Typisches Einsatzszenario

```java
@Component
class WeatherClient {
    @Tool(name = "weather", description = "Aktuelles Wetter für eine Stadt abrufen")
    public WeatherResponse current(@ToolParam("city") String city) {
        return callExternalApi(city);
    }
}

ToolCallbackProvider provider = new StaticToolCallbackProvider();
ChatClient chat = new OpenAiChatClient(model, provider);
```
Zur Laufzeit übergibt der Chat‑Client die Tool‑Liste an das Modell. Sobald das Modell eine *tool call*‑Nachricht liefert, sucht der Resolver das passende Callback, führt es aus und gibt die konvertierte Antwort zurück.

## Erweiterungsmöglichkeiten
1. **Eigene Serialisierung** – `ToolCallResultConverter` implementieren und als Spring‑Bean registrieren.
2. **Erweitertes Fehlerhandling** – eigenen `ToolExecutionExceptionProcessor` bereitstellen.
3. **Hybrider Resolver** – lokalen Resolver mit einem Remote‑Resolver kombinieren und in `DelegatingToolCallbackResolver` einhängen.

## Version & Lizenz
Quellcode unter Apache 2.0 (siehe Header). Zielplattform: **Spring AI 1.0.0** und Java 17.

## Weiterführende Links
* [Spring AI](https://spring.ai)
* [Micrometer Observation](https://micrometer.io/docs/observation)
* [JSON Schema](https://json-schema.org/)
