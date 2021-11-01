package pl.emil.common.utils.doc

import org.springdoc.core.fn.builders.operation.Builder
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * Based on [RouterFunctionDsl](org.springframework.web.reactive.function.server.RouterFunctionDsl)
 * @param T documented class type
 * @param docClass documentation class
 * @param routes sub routes
 * @author emil.wozniak.2020@gmail.com
 * @see org.springframework.web.reactive.function.server.RouterFunctionDsl
 */
fun <T> docRouter(docClass: Class<T>, routes: DocRouterDsl<T>.() -> Unit) =
    DocRouterDsl(docClass, routes).build()

class DocRouterDsl<T> internal constructor(
    private val documentationClassLocation: Class<T>,
    private val init: DocRouterDsl<T>.() -> Unit,
) {

    @PublishedApi
    internal val builder = SpringdocRouteBuilder.route()

    internal fun build(): RouterFunction<ServerResponse> {
        init()
        return builder.build()
    }

    fun GET(
        path: String,
        beanMethodName: String,
        handlerFun: HandlerFunction<ServerResponse>,
        operationsConsumer: Consumer<Builder> = operationsConsumer(documentationClassLocation, beanMethodName),
    ) = builder.GET(path, handlerFun, operationsConsumer)

    fun POST(
        path: String,
        beanMethodName: String,
        handlerFun: HandlerFunction<ServerResponse>,
        operationsConsumer: Consumer<Builder> = operationsConsumer(documentationClassLocation, beanMethodName),
    ) = builder.POST(path, handlerFun, operationsConsumer)

    fun PUT(
        path: String,
        beanMethodName: String,
        handlerFun: HandlerFunction<ServerResponse>,
        operationsConsumer: Consumer<Builder> = operationsConsumer(documentationClassLocation, beanMethodName),
    ) = builder.PUT(path, handlerFun, operationsConsumer)

    fun  DELETE(
        path: String,
        beanMethodName: String,
        handlerFun: HandlerFunction<ServerResponse>,
        operationsConsumer: Consumer<Builder> = operationsConsumer(documentationClassLocation, beanMethodName),
    ) = builder.DELETE(path, handlerFun, operationsConsumer)

    fun String.nest(init: DocRouterDsl<T>.() -> Unit) {
        builder.path(this, Supplier
        { DocRouterDsl(documentationClassLocation, init).build() },
            operationsConsumer(documentationClassLocation)
        )
    }

    private fun <T> operationsConsumer(clazz: Class<T>, beanMethodName: String = "") =
        Consumer<Builder> { ops: Builder ->
            ops.beanClass(clazz).beanMethod(beanMethodName)
        }

    @Deprecated("it's better to d=not declare class everywhere",
        ReplaceWith("POST(path, beanMethodName, handlerFun, operationsConsumer)")
    )
    inline fun <reified T> docPost(
        path: String,
        beanMethodName: String,
        handlerFun: HandlerFunction<ServerResponse>,
        operationsConsumer: Consumer<Builder> = operationsConsumer<T>(beanMethodName),
    ) = builder.POST(path, handlerFun, operationsConsumer)

    @Deprecated("it's better to d=not declare class everywhere",
        ReplaceWith("operationsConsumer(clazz, beanMethodName)")
    )
    inline fun <reified T> operationsConsumer(beanMethodName: String) =
        Consumer<Builder> { ops: Builder ->
            ops.beanClass(T::class.java).beanMethod(beanMethodName)
        }
}

