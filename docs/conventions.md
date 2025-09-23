# 0. Notation

- `<required>` = Parameter, Freifeld welches immer angegeben werden muss
- `[optional]` = Parameter, Freifeld welches nicht zwingend angegeben werden muss
- `|` = Oder; auflisten von alternativen Werten

# 1. Formatierung

Die meisten Regeln in diesem Abschnitt werden standardmäßig von der IDE so gehandhabt.

## 1.1 Allgemein

- Einzug: 4 Leerzeichen
- Maximale Zeichenlänge: 100 Zeichen
- nur eine öffentliche Klasse oder Interface pro Datei (andere private Klassen, etc. immer UNTER die öffentliche Klasse)

## 1.2 Zeilenumbrüche

Wenn ein Ausdruck nicht in eine Zeile passt, sollte er nach folgenden Regeln aufgeteilt werden:

- Umbruch nach einem Komma
- Umbruch vor einem Operator
- Bei mehreren Methodenaufrufen, vor dem Punkt
- Umbrüche auf einer höheren Ebene sind Umbrüchen auf niedrigeren Ebenen vorzuziehen
- die neue Zeile sollte um 8 Leerzeichen eingerückt sein

### Beispiele:

```java
// ✅ Bevorzugt
var = function1(longExpression1,
		function2(longExpression2, longExpression3));

// ❌ Vermeide den Einzug anhand des Ausdrucksstart 
// der vorherigen Zeile zu wählen
var = function1(longExpression1,
				function2(longExpression2, longExpression3));


// ✅ Bevorzugt, weil der Umbruch außerhalb der Klammer passiert (höhere Ebene)
longName1 = longName2 * (longName3 + longName4 - longName5) 
		+ 4 * longname6;

// ❌ Vermeide
longName1 = longName2 * (longName3 + longName4 
		- longName5) + 4 * longname6;


// ❌ Vermeide
Picasso.with(context).load("http://ribot.co.uk/images/sexyjoe.jpg").into(imageView);

// ✅ Bevorzugt
Picasso.with(context)
        .load("http://ribot.co.uk/images/sexyjoe.jpg")
        .into(imageView);


// ❌ Vermeide
loadPicture(context, "http://ribot.co.uk/images/sexyjoe.jpg", mImageViewProfilePicture, clickListener, "Title of the picture");

// ✅ Bevorzugt
loadPicture(context,
        "http://ribot.co.uk/images/sexyjoe.jpg",
        mImageViewProfilePicture,
        clickListener,
        "Title of the picture");
```

## 1.3 Klammern

- Klammer werden in der gleichen Zeile, wie der Code vorher, geöffnet.
- Head und die öffnene Klammer wird durch ein Leerzeichen getrennt

```java
class MyClass {
    int func() {
        if (something) {
            // ...
        } else if (somethingElse) {
            // ...
        } else {
            // ...
        }
    }
}
```

Klammern um Anweisungen werden nicht benötigt, wenn Bedingung und Rumpf in eine Zeile passen

```java
// ✅ Gut
if (condition) body();

// ❌ Schlecht
if (condition)
    body();
```

# 2. Bennenung

| Element | Format | Beispiel |
|---------|--------|----------|
| Klassen, Interfaces | PascalCase | `MainActivity`, `UserDao` |
| Variablen, Methoden | camelCase | `userName`, `fetchData()` |
| Konstanten | SCREAMING_SNAKE_CASE | `MAX_RETRIES` |
| Ressourcen (Strings, Farben, Layouts, etc.) | snake_case | `ab_stacket.9.png`, `activity_user_profile.xml` |

- Klassen sind typischerweise Nomen (`Character`, `ImmutableList`)
- Testklassen: Klassenname + Test (`HashImpl` -\> `HashImplTest`)
- Methoden beginnen mit einem Verben (`sendMessage`)

## 2.1 Abkürzungen sollten als Wort behandelt werden

| Gut | Schlecht |
|-----|----------|
| `XmlHttpRequest` | `XMLHTTPRequest` |
| `getCustomerId` | `getCustomerID` |
| `String url` | `String URL` |
| `long id` | `long ID` |

## 2.2 Dateinamen

### 2.2.1 Klassen

Klassen sollten im `PascalCase` geschrieben werden

Klassen die eine Android-Komponente erweitern, sollen mit dem Namen der Komponete enden, beispielsweise: `SignInActivity`, `SignInFragment`, `ImageUploaderService`, `ChangePassword Dialog`

### 2.2.2 Interfaces und Enums

Interfaces sind typischerweise Nomen oder Adjektive (`List`, `Readable`), hierbei wird zusätzlich ein Prefix eingefügt.

Bei Interfaces ein I, z.B.: `IHealable` Bei Enums ein E, z.B.: `EDirection`

# 3. Dokumentation

## 3.1 Kommentare

- Nur wo notwendig und nicht selbsterklärend
- Keine offensichtlichen Kommentare wie:
- `TODO:` für fehlende Features, die später implementiert werden
- `FIXME:` für Probleme im aktuellen Code (Ineffiziente Implementierung, Bug, unnötiger Code)

## 3.2 Javadoc

- Dokumentation für Klassen, Attribute und Methoden ist verpflichtend
- Javadoc verwendet HTML zur Formatierung

### 3.2.1 Struktur

- **Kurze Zusammenfassung** im ersten Satz
- **Beschreibungsteil**:
    - Vollständige, implementierungsunabhängige Beschreibung der API
    - Einschluss von Randbedingungen, Parameterbereichen und Sonderfällen
    - Absätze mit `<p>` trennen
    - Genug Informationen, damit eine externe Person fundierte Tests schreiben kann
    - Falls notwendig, Implementierungsdetails in einem separaten, klar gekennzeichneten Abschnitt mit Hinweis wie: `Implementation-specific`

### 3.2.2 Stil & Form

- **Dritte Person, deklarativer Stil** statt Imperativ in der zweiten Person\
  :white_check_mark: _Gets the label_  :x: _Get the label_
- **Methodenbeschreibungen beginnen mit einem Verb**\
  :white_check_mark: _Gets the label of this button_  :x: _This method gets the label of this button_
- **Klassen-, Interface- und Attributbeschreibungen** dürfen ohne Subjekt beginnen\
  :white_check_mark: _A button label_  :x: _This field is a button label_
- Die Beschreibung soll **mehr liefern** als nur eine Wiederholung des Methodennamens
- Verwende `<code>...</code>` zur Hervorhebung von Java-Elementen wie Keywords, Klassen, Methoden, Feldern etc.
- Verlinkungen per `{@link ...}` sparsam und nur wenn sie Mehrwert bringen
- Bei Methodenverweisen **keine Klammern**, es sei denn, es gibt mehrere Implementierungen

### 3.2.3 Tags

Tags sollten in der folgenden Reihenfolge auftauchen:

- `@author <NAME>` (nur Klassen und Interfaces, **erforderlich**)
- `@version <VERSION>` (nur Klassen und Interfaces, **erforderlich**)
- `@param <PARAMETER>` (nur Methoden und Konstruktor, **erforderlich für jeden Parameter**)
- `@return` (nur Methoden, **erforderlich für einen Rückgabewert anders als void**)
- \`@throws ( oder @exception)
- `@see <REF>`
- `@since <VERSION>`
- `@serial` (oder @serialField oder @serialData)
- `@deprecated` (mehr Infos: [How and When To Deprecate APIs](https://docs.oracle.com/javase/7/docs/technotes/guides/javadoc/deprecation/deprecation.html))

**Mehrere Tags**

- mehrere Autoren alphabetisch sortieren
- mehrere Parameter in Deklarationsreihenfolge
- mehrere Exceptions alphabetisch sortiert

# 4. Logging

- TAG als Konstante am Anfang der Klasse deklarieren
- Sinnvolle Verwendung der Level: Debug, Info, Warn, Error
- `Log.d(...)` muss immer mit der Bedingung `BuildConfig.DEBUG` verwendet werden

```java
public class MyClass {
    private static final String TAG = MyClass.class.getSimpleName();

    public myMethod() {
        Log.e(TAG, "My error message");
        if (BuildConfig.DEBUG) Log.d(TAG, "My debug message")
    }
}
```

# 5. Java Regeln

## 5.1 Keine Exceptions ignorieren

Folgendes sollte immer vermieden werden:

```java
void setServerPort(String value) {
    try {
        serverPort = Integer.parseInt(value);
    } catch (NumberFormatException e) { }
}
```

Auch wenn die Annahme besteht, dass dieser Fehler in dem Code nicht auftritt oder seine Behebung nicht relevant ist, resultiert die Ignorierung von Ausnahmen in gefährlichen Fallen im Code, auf die eines Tages jemand stoßen wird (Technische Schulden). Jede Ausnahmen sollte prinzipell im Code behandelt werden. Die korrekte Behandlung variiert je nach Fall, siehe auch [Android Conventions](https://source.android.com/docs/setup/contribute/code-style#dont-ignore-exceptions).

## 5.2 Keine generischen Exceptions abfangen

Folgendes sollte immer vermieden werden:

```java
try {
    someComplicatedIOFunction();        // may throw IOException
    someComplicatedParsingFunction();   // may throw ParsingException
    someComplicatedSecurityFunction();  // may throw SecurityException
    // phew, made it all the way
} catch (Exception e) {                 // I'll just catch all exceptions
    handleError();                      // with one generic handler!
}
```

Das Abfangen generischer Exceptions oder Throwable sollte vermieden werden, da es zu einer unsauberen und unübersichtlichen Fehlerbehandlung führt.

Es gibt nur wenige Ausnahmen, etwa Tess oder ganz oben in der Anwendungslogik. Es empfiehlt sich, spezifische Ausnahmen gezielt zu behandeln, ausnahmen weiterzugeben.

Für mehr Informationen siehe [Android Conventions](https://source.android.com/docs/setup/contribute/code-style#dont-catch-generic-exception)

## 5.3 Annotationen

### 5.3.1 Klassen, Methoden und Konstruktoren

- nach dem Dokumentationsblock
- eine Annonation pro Zeile

```java
/* This is the documentation block about the class */
@AnnotationA
@AnnotationB
public class MyAnnotatedClass { }
```

### 5.3.2 Attribute

- in der gleichen Zeile, außer die maximale Zeichenlänge wird überschritten

```java
@Nullable @Mock DataManager dataManager;
```

## 5.4 Limitiere den Variablensichbarkeitsbereich

Der Sichbarkeitsbereich einer Variable sollte immer minimal sein. Das erhöht die Lesbarkeit und Wartbarkeit des Code und reduziert die Wahrscheinlichkeit eines Fehlers.

Eine lokale Variable sollte bei ihrer ersten Verwendung deklariert werden. Nahezu alle Deklarationen sollten immer in Kombination mit einer Initialisierung verwendet werden.

Eine Ausnahme stellen Try-Catch-Blöcke dar, wenn ein Wert durch ein Methodenaufruf, der eine Ausnahme wirft, initialisiert werden soll und dieser nach dem Block noch benötigt wird.

Für mehr Informationen siehe [Android Conventions](https://source.android.com/docs/setup/contribute/code-style#limit-variable-scope)

## 5.5 Reihenfolge von Imports

Bei Verwendung einer IDE wie Android Studio ist dies nicht relevant, da die IDE bereits auf diese Regeln eingestellt ist. Sollte dies nicht der Fall sein, finden Sie weitere Informationen unten.

Die Reihenfolge von Imports ist:

1. Spring Imports
2. Imports von Drittanbieter Bibliotheken
3. Java und Javax Imports
4. Eigene Projektimports

Imports sollten in ihrer Gruppe alphabetisch sortiert sein. Großbuchstaben vor Kleinbuchstaben (also Z vor a) Jede Gruppe sollte durch eine leere Zeile getrennt sein

## 5.6 Reihenfolge der Klassenmitglieder

Es gibt keine allgemeingültige Lösung, aber eine logische und konsistente Reihenfolge verbessert die Erlernbarkeit und Lesbarkeit des Codes.

Es wird empfohlen, die folgende Reihenfolge zu verwenden:

1. Konatanten
2. Attribute
3. Konstruktoren
4. Überschriebene Methoden und Callbacks (öffentlich und privat)
5. Öffentliche Methoden
6. Private Methoden
7. Innere Klassen und Interfaces

```java
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String title;
    private TextView textViewTitle;

    @Override
    public void onCreate() {
        ...
    }

    public void setTitle(String title) {
    	title = title;
    }

    private void setUpView() {
        ...
    }

    static class AnInnerClass {

    }

}
```

## 5.7 Reihenfolge von Parametern in Methoden

In Android ist es sehr üblich, dass Methoden einen Kontext als Parameter bekommen. Bei einer solchen Methode sollte der Kontext immer der erste Parameter sein. Genau das gegenteil gilt für Callback Interfaces, diese sollten immer den letzten Parameter bilden.

Beispiel:

```java
// Context always goes first
public User loadUser(Context context, int userId);

// Callbacks always go last
public void loadUserAsync(Context context, int userId, UserCallback callback);
```

## 5.8 Argumente in Fragments und Aktivities

Im Falle der Übertragung von Daten in eine `Activity` oder ein `Fragment` mittels `Intent` oder `Bundle`, müssen die Schlüssel die Vorgaben des [Abschnitts 2.2](#23-string-konstanten-benennung-und-werte) befolgen.

Wenn eine `Activity` oder ein `Fragment` ein Parameter erwartet, sollte es eine öffentliche statische Methode bereitstellen, die die Erstellung eines `Intents` oder `Fragment` übernimmt.

In Fall einer Aktivität wird die Methode normalerweise `getStartIntent()` genannt:

```java
public static Intent getStartIntent(Context context, User user) {
	Intent intent = new Intent(context, ThisActivity.class);
	intent.putParcelableExtra(EXTRA_USER, user);
	return intent;
}
```

Bei Fragments wird diese Methode `newInstance()` genant und erstellt ein Fragment mit den richtigen Argumenten:

```java
public static UserFragment newInstance(User user) {
	UserFragment fragment = new UserFragment();
	Bundle args = new Bundle();
	args.putParcelable(ARGUMENT_USER, user);
	fragment.setArguments(args)
	return fragment;
}
```

# 6. XML Regeln

## 6.1 Selbstschließende Tags

Besitzt ein XML-Element keine weiteren Elemente muss ein selbstschließendes Tag verwendet werden.

> \[!note\] Vor dem Slash des schließenden Tags sollte ein Leerzeichen eingefügt werden

```xml
<!-- ✅ Bevorzuge -->
<TextView
	android:id="@+id/text_view_profile"
	android:layout_width="wrap_content"
	android:layout_height="wrap_content" />

<!-- ❌ Vermeinde -->
<TextView
    android:id="@+id/text_view_profile"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" >
</TextView>
```

# 7. Commits

## 7.1 Aufbau einer Commitnachricht

```
<type>[optional scope]: <Zusammenfassung>

[optional Beschreibung]

[optional footer(s): Fußnoten]
```

### 7.1.1 Zusammenfassung

- Kurze Zusammenfassung der Änderungen
- Imperativ, Präsens: `change` nicht `changed` oder `changes`
- das erste Wort sollte nicht großgeschrieben werden
- die Zusammenfassung wird nicht mit einem Punkt beendet

### 7.1.2 Beschreibung

- optional
- Motivation hinter den Änderungen
- Unterschied zu dem vorherigen Verhalten
- Imperativ, Präsens: `change` nicht `changed` oder `changes`

### 7.1.3 Fußnoten

- optional
- Referenzen auf Issus, Tickets (Beispiel: `Closes #123` oder `Fixes JIRA-456`)
- Informationen über BREAKING CHANGES

## 7.2 Typen

| Typ | Beschreibung |
|-----|--------------|
| build | Änderung, die das Build-System oder externe Abhängigkeiten betreffen |
| ci | Änderungen an der CI/CT Pipeline |
| docs | Änderungen nur an der Dokumentation |
| feat | Eine neue Funktion |
| fix | Eine Fehlerbehebung |
| perf | Eine Codeänderung, die die Leistung verbessert |
| refactor | Eine Codeänderung, die weder Fehler behebt noch eine Funktion hinzufügt |
| style | Änderungen, die die Bedeutung des Codes nicht beeinflussen (Leerzeichen, Formatierung, fehlende Semikolons) |
| test | Hinzufügen fehlender Tests, Korrigieren vorhandener Tests |

## 7.3 Versionierung

xx.yy.zz (Major.Minor.Patch)

- BREAKING CHANGE -\> Major
- API relevante Änderungen -\> Minor
- Ansonsten -\> Patch

## 7.4 Beispiele:

**Commitnachricht mit ! und BREAKING CHANGE Fußnote**

```
chore!: drop support for Node 6

BREAKING CHANGE: use JavaScript features not available in Node 6.
```

**Commitnachricht mit Scope**

```
feat(lang): add Polish language
```

**Commitnachricht mit Mehrzeiligem Body und mehreren Fußnoten**

```
fix: prevent racing of requests

Introduce a request id and a reference to latest request. Dismiss
incoming responses other than from latest request.

Remove timeouts which were used to mitigate the racing issue but are
obsolete now.

Reviewed-by: Z
Refs: #123
```

Vollständige Konvention: [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/#summary)

# 8. Branches

### Syntax

```java
<typ>/<beschreibung>
```

Als Branch Typen können die [Typen](https://git.thm.de/swt-mt-ss25/teama/-/wikis/home/conventions#72-typen) der Commit Nachrichten verwendet werden.

### Beschreibung

Eine sinnvolle aber kurze Beschreibung / Zusammenfassung der Funktion der Branch. Mehrere Wörter werden durch Bindestriche getrennt.

### Beispiele

```
feat/user-authentication
fix/fix-header-bug
chore/update-dependencies
```

Vollständige Konvention: [Conventional Branch](https://conventional-branch.github.io/)

# 9. Tests

## 9.1 Unittests

- Testklassen = Klassennamen + Postfix `Test`
- Methoden werden mit `@Test` annotiert
- Methodenname = orginaler Name + \[Bedingung\] + \[Erwartetes Verhalten\]

Bedingung und/oder erwartetes Verhalten werden nicht immer benötigt

Template:

```java
@Test 
public void methodNamePreconditionExpectedBehaviour() {}
```

Beispiel:

```java
@Test 
public void signInWithEmptyEmailFails() {}
```

In manchen Fällen werden für eine Klasse sehr viele Test benötigt. Teilweise mehrere Tests für eine Methode. In diesem Fall ist es ratsam die Testklasse in mehrere Klassen aufzuteilen. Dies wird vorallem für parameterisierte Tests benötigt.

Beispiel: `DataManagerSignInTest` und `DataManagerLoadUsersTest`

Mehr Infos über Unit Tests: [JUnit4 Wiki](https://github.com/junit-team/junit4/wiki)

# Referenzen & Quellen

- https://www.oracle.com/docs/tech/java/codeconventions.pdf
- https://www.oracle.com/de/technical-resources/articles/java/javadoc-tool.html
- https://google.github.io/styleguide/javaguide.html
- https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md
- https://source.android.com/docs/setup/contribute/code-style
- https://github.com/junit-team/junit4/wiki
- https://www.conventionalcommits.org/en/v1.0.0/
- https://gist.github.com/qoomon/5dfcdf8eec66a051ecd85625518cfd13
- https://conventional-branch.github.io