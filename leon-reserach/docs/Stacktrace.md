# Stacktrace

## Startup

The auto-configuration will automatically detect and register all tool callbacks from: 
- Individual ToolCallback beans 
- Lists of ToolCallback beans 
- ToolCallbackProvider beans 

([*src*](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html?utm_source=chatgpt.com#_tools))

## Detection: individual ToolCallback
```
@Bean
public ToolCallback weatherTool() {
    return new WeatherTool();
}
```

## Detection: List of TollCallBack

```
@Bean
public List<ToolCallback> shoppingCartToolCallbacks(ShoppingCart shoppingCart) {
    return List.of(ToolCallbacks.from(shoppingCart));
}
 ```

### ToolCallbacks.from(<sources>) 
Provides `{@link ToolCallback}` instances for tools defined in different sources.

## Detection: ToolCallbackProvider
```
@Bean
public ToolCallbackProvider myProvider() {
    return () -> List.of(new WeatherTool(), new BookingTool());
}
```
Encapsulates the tool-creation in a provider




## ListableBeanFactory

With the  [ListableBeanFactory](./src/spring-framework-main/spring-beans/src/main/java/org/springframework/beans/factory/ListableBeanFactory.java) spring is able to collect Tools

