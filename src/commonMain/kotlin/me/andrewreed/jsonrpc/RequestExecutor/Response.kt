package me.andrewreed.jsonrpc.RequestExecutor

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import me.andrewreed.jsonrpc.Client.RequestId

@Serializable
data class Response(
    val id: RequestId,
    val jsonrpc: String,
    val result: JsonElement? = null,
    val error: Error? = null
)

@Serializable
data class Error(
    val message: String,
    val code: Int,
    val data: JsonObject? = null
) {
    override fun toString(): String {
        return "{" +
            "message" + ":" + message + " " +
            "code" + ":" + code + " " +
            "data" + ":" + data + " " +
            "}"
    }
}
/*
static let Error = "error"
        static let Code = "code"
        static let Message = "message"
        static let Data = "data"

        {
    "id": 64,
    "jsonrpc": "2.0",
    "error": {
        "message": "Incorrect number of arguments. Method 'web3_sha3' requires exactly 1 arguments. Request specified 0 arguments: [null].",
        "code": -32000,
        "data": {
            "stack": "Error: Incorrect number of arguments. Method 'web3_sha3' requires exactly 1 arguments. Request specified 0 arguments: [null].\n    at GethApiDouble.handleRequest (/Applications/Ganache.app/Contents/Resources/static/node/node_modules/ganache-core/lib/subproviders/geth_api_double.js:106:16)\n    at next (/Applications/Ganache.app/Contents/Resources/static/node/node_modules/ganache-core/node_modules/web3-provider-engine/index.js:136:18)\n    at GethDefaults.handleRequest (/Applications/Ganache.app/Contents/Resources/static/node/node_modules/ganache-core/lib/subproviders/gethdefaults.js:15:12)\n    at next (/Applications/Ganache.app/Contents/Resources/static/node/node_modules/ganache-core/node_modules/web3-provider-engine/index.js:136:18)\n    at SubscriptionSubprovider.FilterSubprovider.handleRequest (/Applications/Ganache.app/Contents/Resources/static/node/node_modules/ganache-core/node_modules/web3-provider-engine/subproviders/filters.js:89:7)\n    at SubscriptionSubprovider.handleRequest (/Applications/Ganache.app/Contents/Resources/static/node/node_modules/ganache-core/node_modules/web3-provider-engine/subproviders/subscriptions.js:137:49)\n    at next (/Applications/Ganache.app/Contents/Resources/static/node/node_modules/ganache-core/node_modules/web3-provider-engine/index.js:136:18)\n    at DelayedBlockFilter.handleRequest (/Applications/Ganache.app/Contents/Resources/static/node/node_modules/ganache-core/lib/subproviders/delayedblockfilter.js:31:3)\n    at next (/Applications/Ganache.app/Contents/Resources/static/node/node_modules/ganache-core/node_modules/web3-provider-engine/index.js:136:18)\n    at RequestFunnel.handleRequest (/Applications/Ganache.app/Contents/Resources/static/node/node_modules/ganache-core/lib/subproviders/requestfunnel.js:32:12)\n    at next (/Applications/Ganache.app/Contents/Resources/static/node/node_modules/ganache-core/node_modules/web3-provider-engine/index.js:136:18)\n    at Web3ProviderEngine._handleAsync (/Applications/Ganache.app/Contents/Resources/static/node/node_modules/ganache-core/node_modules/web3-provider-engine/index.js:123:3)\n    at Timeout._onTimeout (/Applications/Ganache.app/Contents/Resources/static/node/node_modules/ganache-core/node_modules/web3-provider-engine/index.js:107:12)\n    at listOnTimeout (internal/timers.js:531:17)\n    at processTimers (internal/timers.js:475:7)",
            "name": "Error"
        }
    }
}
 */
/*
{
    "id": 64,
    "jsonrpc": "2.0",
    "result": "0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad"
}
 */

/*
public class Response
{
// MARK: Construction

    init(response: Any) throws
    {
        guard let json = (response as? [String: AnyObject]),
              let version = (json[JsonKeys.JsonRPC] as? String), (version == RPCClient.Version),
              let id = (json[JsonKeys.Id] as? String)
        else {
            throw ResponseError.invalidFormat
        }

        // Handle 'id' object
        self.id = id

        // Handle 'result' object if exists
        if let result = json[JsonKeys.Result]
        {
            // Create success result body
            self.body = .success(result: result)
        }
        else
        // Handle 'error' object if exists
        if let error   = (json[JsonKeys.Error] as? [String: Any]),
           let code    = (error[JsonKeys.Code] as? Int),
           let message = (error[JsonKeys.Message] as? String)
        {
            let data = error[JsonKeys.Data]

            // Init JSON-RPC error
            let error = RPCError(code: code, message: message, data: data)

            // Create error body
            self.body = .error(error: error)
        }
        else {
            throw ResponseError.invalidFormat
        }
    }

// MARK: - Properties

    public let id: String

    public let body: Body

// MARK: - Inner Types

    public enum Body
    {
        case success(result: AnyObject)
        case error(error: RPCError)
    }

// MARK: Constants

    fileprivate struct JsonKeys
    {
        static let JsonRPC = "jsonrpc"
        static let Method = "method"
        static let Params = "params"
        static let Result = "result"
        static let Error = "error"
        static let Code = "code"
        static let Message = "message"
        static let Data = "data"
        static let Id = "id"
    }

}

// ----------------------------------------------------------------------------

enum ResponseError: Error
{
    case invalidFormat
}

// ----------------------------------------------------------------------------

 */
