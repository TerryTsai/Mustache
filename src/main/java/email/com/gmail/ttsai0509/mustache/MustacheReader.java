package email.com.gmail.ttsai0509.mustache;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

@SuppressWarnings({"WeakerAccess", "StatementWithEmptyBody", "UnusedAssignment"})
public class MustacheReader extends FilterReader {

    private int taglinePosition;
    private Token[] taglineBacklog;

    private boolean repeatBufferEnabled;
    private final Tokenizer tokenizer;
    private final ContextStack contextStack;
    private final RepeatBuffer repeatBuffer;
    private final CharacterBuffer characterBuffer;

    public MustacheReader(Reader in, Object hash) {
        super(in);
        this.taglinePosition = 0;
        this.taglineBacklog = new Token[5];
        this.repeatBufferEnabled = false;
        this.tokenizer = new Tokenizer(in);
        this.contextStack = new ContextStack(hash);
        this.repeatBuffer = new RepeatBuffer();
        this.characterBuffer = new CharacterBuffer();
    }

    @Override
    public int read() throws IOException {
        char[] buffer = new char[1];
        int read = read(buffer, 0, 1);
        return read > 0 ? buffer[0] : -1;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int read = 0;
        int limit = Math.min(cbuf.length - off, len);
        boolean isEOF = false;

        do {
            if (characterBuffer.hasNext()) {
                // Immediately read buffered data
                read += characterBuffer.read(cbuf, off + read, limit - read);

            } else if (repeatBuffer.hasNext()) {
                // Immediately process buffered token
                isEOF = processToken(repeatBuffer.next(), repeatBuffer.position(), true);

            } else {
                // Process new token from reader
                isEOF = processToken(tokenizer.readToken(), -1, false);
            }
        } while (!isEOF && read < limit);

        return (isEOF && read == 0) ? -1 : read;
    }

    private void processEnd(Token token) throws IOException {
        // Sanity check that section tokens match
        int location = contextStack.top().getLocation();
        Token start = repeatBuffer.getToken(location);
        if (!Token.matches(start, token)) {
            throw new IOException(String.format("Expected closing token for %s, but saw %s", start, token));
        }

        if (contextStack.top().canShift()) {
            // Repeat section for next item
            contextStack.top().shift();
            repeatBuffer.moveTo(contextStack.top().getLocation() + 1);

        } else {
            // Finished with section, continue
            contextStack.pop();
            if (contextStack.top().isRoot()) {
                repeatBuffer.clear();
                repeatBufferEnabled = false;
            }
        }
    }

    private void processText(Token token, boolean isVisible) {
        if (isVisible)
            characterBuffer.set(token.content);
    }

    private void processVariable(Token token, boolean isVisible) {
        if (isVisible) characterBuffer.set(contextStack.render(token.content));
    }

    private void processInverse(Token token, int bufferLocation) {
        contextStack.push(token.content, bufferLocation, true);
    }

    private void processSection(Token token, int bufferLocation) {
        contextStack.push(token.content, bufferLocation, false);
    }

    private boolean processToken(Token token, int bufferLocation, boolean isBuffered) throws IOException {
        // Determine if this token may need to be repeated
        if (!isBuffered && (repeatBufferEnabled || token.type == Token.Type.SECTION || token.type == Token.Type.INVERSE)) {
            bufferLocation = repeatBuffer.addToken(token);
            repeatBufferEnabled = true;
        }

        // Determine if we will be adding anything to the characterBuffer
        boolean isVisible = contextStack.top().isVisible();

        // Determine action to take for the given token
        switch (token.type) {
            case EOF: break;
            case COMMENT: break;
            case END: processEnd(token); break;
            case TEXT: processText(token, isVisible); break;
            case NEWLINE: processText(token, isVisible); break;
            case VARIABLE: processVariable(token, isVisible); break;
            case INVERSE: processInverse(token, bufferLocation); break;
            case SECTION: processSection(token, bufferLocation); break;
        }

        return token.type == Token.Type.EOF;
    }

    private boolean checkTaglineWait(Token token) {
        // Add this feature later
        boolean a = taglinePosition == 0 && token.type == Token.Type.NEWLINE;
        boolean b = taglinePosition == 1 && token.type == Token.Type.TEXT && token.isWhitespace();
        boolean c = taglinePosition == 2 && (token.type == Token.Type.SECTION || token.type == Token.Type.INVERSE);
        boolean d = taglinePosition == 3 && token.type == Token.Type.TEXT && token.isWhitespace();
        boolean e = taglinePosition == 4 && token.type == Token.Type.NEWLINE;

        if (a || b || c || d || e) {
            taglineBacklog[taglinePosition++] = token;
            return true;
        } else {
            return false;
        }
    }

}