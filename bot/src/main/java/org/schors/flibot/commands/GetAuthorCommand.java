/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2016 schors
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package org.schors.flibot.commands;

import io.vertx.core.Handler;
import org.schors.flibot.Search;
import org.schors.flibot.SearchType;
import org.schors.flibot.SendMessageList;
import org.schors.vertx.telegram.bot.commands.BotCommand;
import org.schors.vertx.telegram.bot.commands.CommandContext;

@BotCommand(message = "^/author")
public class GetAuthorCommand extends FlibotCommand {

    public GetAuthorCommand() {
    }

    @Override
    public void execute(CommandContext context, Handler<Boolean> handler) {
        log.warn("## Get author command executing: " + context.getUpdate());
        String userName = context.getUpdate().getMessage().getFrom().getUsername();
        Search search = getSearches().get(userName);
        if (search != null) {
            getSearches().remove(userName);
            doGenericRequest("/opds" + String.format(authorSearch, search.getToSearch()), event -> {
                if (event.succeeded()) {
                    sendReply(context, (SendMessageList) event.result());
                    handler.handle(Boolean.TRUE);
                } else {
                    sendReply(context, "Error happened :(");
                    handler.handle(Boolean.FALSE);
                }
            });
        } else {
            search = new Search();
            search.setSearchType(SearchType.AUTHOR);
            getSearches().put(userName, search);
            sendReply(context, "Please enter the author name to search");
            handler.handle(Boolean.TRUE);
        }
    }
}