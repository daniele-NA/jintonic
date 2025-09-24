



IMMAGINE  dell'app di test,logo della libreria


    /*

    JinTonic.checkNetwork().requireBattery(20).execute { apiCall() }

     */



    /* CLASSI:
    @Before("within(@com.crescenzi.jintonic.DebugLog *)")


     */

@Around("execution(@com.crescenzi.jintonic.DebugLog * *(..))")
fun debug_log(proceedingJoinPoint: ProceedingJoinPoint) {
LOG_E("LOG Before proceed()")
proceedingJoinPoint.proceed()
}

//    @Before("execution(* me.crescenzi.*.*.onCreate(android.os.Bundle))")
//    fun onCreateAdvice(joinPoint: JoinPoint?) {
//        if (joinPoint?.getThis() != null) {
//            LOG("Activity Event")
//        }
//    }
//
//
//    @Pointcut("execution(void *.onClick(..))")
//    fun onButtonClick() {
//    }
//
//    @Before("onButtonClick() && args(view)")
//    fun onClickAdvice(view: View?) {
//        if (view is TextView) {
//            // Logger.logItem("${view.text} clicked")
//        }
//    }


# 📑 AspectJ Pointcut Cheat-Sheet

| Sintassi                           | Significato                                         | Esempio                                         |
| ---------------------------------- | --------------------------------------------------- | ----------------------------------------------- |
| `execution(* *(..))`               | Tutti i metodi (qualsiasi ritorno, nome, parametri) | `fun foo()`, `fun bar(x: Int)`                  |
| `execution(* com.pkg.Clazz.*(..))` | Tutti i metodi di una classe specifica              | `Clazz.method1()`, `Clazz.method2()`            |
| `execution(* com.pkg..*(..))`      | Tutti i metodi in un package e sottopacchetti       | `com.pkg.A.foo()`, `com.pkg.sub.B.bar()`        |
| `execution(public * *(..))`        | Tutti i metodi pubblici                             | esclude privati/protetti                        |
| `execution(* *(String,..))`        | Metodi con primo argomento `String`                 | `fun test("abc", 123)`                          |
| `execution(@Anno * *(..))`         | Metodi annotati con `@Anno`                         | `@DebugLog fun foo()`                           |
| `@annotation(Anno)`                | Join point con metodo annotato                      | idem sopra, sintassi alternativa                |
| `within(com.pkg..*)`               | Tutti i join point all’interno di un package        | qualsiasi classe in `com.pkg`                   |
| `within(@Anno *)`                  | Tutte le classi annotate con `@Anno`                | `@Service class MyClass {}`                     |
| `this(Type)`                       | Join point eseguiti da istanze di tipo `Type`       | oggetto corrente è `Activity`                   |
| `target(Type)`                     | Join point dove il metodo appartiene a `Type`       | chiamata verso oggetto `Type`                   |
| `args(Type,..)`                    | Metodi con determinati tipi di parametri            | `args(String)` → metodi con `String`            |
| `call(* *(..))`                    | Quando un metodo viene chiamato (dal chiamante)     | `obj.method()` intercettato lato chiamata       |
| `execution(* *(..))`               | Quando un metodo viene eseguito (nel corpo)         | intercetta **dentro** il metodo                 |
| `get(* *)`                         | Accesso in lettura a un campo                       | `val x = obj.field`                             |
| `set(* *)`                         | Accesso in scrittura a un campo                     | `obj.field = 123`                               |
| `initialization(Type.new(..))`     | Costruttore in esecuzione                           | `new MyClass(..)`                               |
| `cflow(execution(..))`             | Join point dentro il flusso di un altro             | tracing di chiamate annidate                    |
| `Type+`                            | Matcha un tipo e tutte le sue sottoclassi           | `Activity+` = `Activity` + tutte le sottoclassi |
| `*`                                | Qualsiasi nome/tipo singolo                         | `* foo(..)` → qualsiasi ritorno, metodo `foo`   |
| `..`                               | “zero o più” package o argomenti                    | `com..*` = qualsiasi sottopacchetto             |

---


root:


buildscript {
repositories {
google()
mavenCentral()
maven("https://jitpack.io")
}
dependencies {
classpath("com.ibotta:plugin:1.4.1")
}
}


module:

apply(plugin = "com.ibotta.gradle.aop")

STANDALONE :

    //id("com.ibotta.gradle.aop") version "1.4.1"