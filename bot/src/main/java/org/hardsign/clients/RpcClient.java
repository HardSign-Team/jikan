package org.hardsign.clients;

import okhttp3.OkHttpClient;
import org.hardsign.models.settings.BotSettings;

import java.util.function.Supplier;

public class RpcClient extends RpcBaseClient {
    public RpcClient(OkHttpClient client, String baseUrl, Supplier<BotSettings> settingsProvider) {
        super(client, baseUrl, settingsProvider);
    }
}
