import java.util.*;
import java.io.*;
public class JsonCheckKit {
    private enum StatusEnum {
        INITIAL, OBJ_EMPTY, NV_NAME, COLON, VALUE,
		ARRAY_EMPTY, NEXT_ELE, STRING, VALUE_END,
        NUMBER, BOOLSTR, NULLSTR, FINISH
    };
    private	enum ErrorTypeEnum {
            NO_ERROR, WRONG_START, WRONG_NOT_MATCH,
            WRONG_NV_NAME, WRONG_VALUE_TYPE, WRONG_NUM,
            WRONG_ESC_STRING, WRONG_END, WRONG_MISS_DELIMITER, WRONG_MISS_COLON
        };
    private String jstr = "";
    private int index = 0;
    private int preIndex = 0;
    private Stack<Character> sc = new Stack<Character>();
    private StatusEnum test = StatusEnum.INITIAL;
    private ErrorTypeEnum errorType = ErrorTypeEnum.NO_ERROR;
    
    public void resetMembers() {
        jstr = "";
        index = 0;
        preIndex = 0;
        while(!sc.empty()) {
            sc.pop();
        }
        test = StatusEnum.INITIAL;
        errorType = ErrorTypeEnum.NO_ERROR;
    }

    public boolean jsonCheckFromString(String tmpStr) {
        jstr = tmpStr;
        while(index < jstr.length()) {
            switch (test) {
                case INITIAL: {
                    if (INITIALCheck())
                        break;
                    else
                        return false;
                }
                case OBJ_EMPTY: {
                    if (OBJ_EMPTYCheck())
                        break;
                    else
                        return false;
                }
                case NV_NAME: {
                    if (NV_NAMECheck())
                        break;
                    else
                        return false;
                }
                case COLON: {
                    if (COLONCheck())
                        break;
                    else
                        return false;
                }
                case ARRAY_EMPTY: {
                    if (ARRAY_EMPTYCheck())
                        break;
                    else
                        return false;
                }
                case VALUE: {
                    if (VALUECheck())
                        break;
                    else
                        return false;
                }
                case NEXT_ELE: {
                    if (NEXT_ELECheck())
                        break;
                    else
                        return false;
                }
                case STRING: {
                    if (STRINGCheck())
                        break;
                    else
                        return false;
                }
                case NUMBER: {
                    if (NUMBERCheck())
                        break;
                    else
                        return false;
                }
                case NULLSTR: {
                    if (NULLSTRCheck())
                        break;
                    else
                        return false;
                }
                case BOOLSTR: {
                    if (BOOLSTRCheck())
                        break;
                    else
                        return false;
                }
                case VALUE_END: {
                    if (VALUE_ENDCheck())
                        break;
                    else
                        return false;
                }
                case FINISH: {
                    if (FINISHCheck())
                        break;
                    return false;
                }
            }
        }
        if (sc.empty() ) {
            return true;
        }
        if (errorType == ErrorTypeEnum.NO_ERROR) {
            errorType = ErrorTypeEnum.WRONG_NOT_MATCH;
        }
        return false;
    }

    public boolean jsonCheckFromStringShowError(String tmpStr) {
        boolean returnValue = jsonCheckFromString(tmpStr);
        if (errorType != ErrorTypeEnum.NO_ERROR) {
            printInfo();
        }
        switch (errorType)
        {
        case NO_ERROR: {
            System.out.println("格式正确");
            break;
        }
        case WRONG_START: {
            System.out.println("期望是:{ 或 [");
            break;
        }
        case WRONG_NOT_MATCH: {
            char  tmp = ' ';
            switch (sc.peek()) {
            case '[':
                tmp = ']';
                break;
            case '{' :
                tmp = '}';
                break;
            case '"':
                tmp = '"';
                break;
            }
            System.out.println("对称字符不匹配，期望是:" + tmp);
            break;
        }
        case WRONG_MISS_COLON:
        case WRONG_NV_NAME: {
            System.out.println("键值对格式错误，期望是:\"string\":value");
            break;
        }
        case WRONG_VALUE_TYPE: {
            System.out.println("value类型错误，期望是:string/object/array/number/bool/null中的一种");
            break;
        }
        case WRONG_NUM: {
            System.out.println("数字格式错误");
            break;
        }
        case WRONG_ESC_STRING: {
            System.out.println("字符串中转义字符有错误");
            break;
        }
        case WRONG_END: {
            System.out.println("错误的结束符");
            break;
        }
        case WRONG_MISS_DELIMITER: {
            System.out.println("缺少分隔符错误，期望是:','");
            break;
        }
        }    
        return returnValue;
    }

    public boolean jsonCheckFromFile(String fileName) {
        
        int pos = fileName.lastIndexOf('.');
        if(pos == -1) {
            System.out.println("缺少文件类型");
            return false;
        }
        String fileType = fileName.substring(pos, fileName.length());
        fileType = fileType.toLowerCase();
        if(!fileType.equals(".json")) {
            System.out.println("不是JSON文件");
            return false;
        }
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在");
        }
        int lineNum = 0;
        try {
            String buffer = new String();
            boolean haveNotRead = true;
            while(true) {
                if(haveNotRead) {
                    buffer = br.readLine();
                }
                if(buffer != null) {
                lineNum++;
                } else break;
                boolean returnValue = jsonCheckFromString(buffer);
                haveNotRead = true;
                if(returnValue == true) {
                    continue;
                }
                boolean notMatch = (errorType == ErrorTypeEnum.WRONG_NOT_MATCH);
                boolean missColon = (errorType == ErrorTypeEnum.WRONG_MISS_COLON);
                if(notMatch || missColon) {
                    haveNotRead = false;
                    if((buffer = br.readLine()) != null) {
                    index = 0;
                    errorType = ErrorTypeEnum.NO_ERROR;
                    } else {
                        System.out.println(lineNum);
                        return false;
                    }
                } else {
                    System.out.println(lineNum);
                    return false;
                }
            }
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
        
        try {
        fr.close();
        br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean INITIALCheck() {
        while (index < jstr.length() && isSpace(jstr.charAt(index))) {
            index++;
        }
    
        if (index == jstr.length()) {
            errorType = ErrorTypeEnum.WRONG_START;
            return false;
        }
    
        if (jstr.charAt(index) == '{') {
            sc.push('{');
            //ss.push(OBJECT);
            test = StatusEnum.OBJ_EMPTY;
        } else if (jstr.charAt(index) == '[') {
            sc.push('[');
            //ss.push(ARRAY);
            test = StatusEnum.ARRAY_EMPTY;
        } else {
            errorType = ErrorTypeEnum.WRONG_START;
            return false;
        }
    
        index++;
        return true;
    }

    private boolean OBJ_EMPTYCheck() {
        while (index < jstr.length() && isSpace(jstr.charAt(index))) {
            index++;
        }

        if (index == jstr.length()) {
            errorType = ErrorTypeEnum.WRONG_NOT_MATCH;
            return false;
        }
    
        switch (jstr.charAt(index)) {
        case '"': {
            test = StatusEnum.NV_NAME;
            sc.push('"');
            index++;
            return true;
        }
        case '}': {
            sc.pop();
            //ss.pop();
            index++;
            if (sc.empty()) {
                test = StatusEnum.FINISH;
            }
            else {
                test = StatusEnum.VALUE_END;
            }
            return true;
    
        }
        default: {
            errorType = ErrorTypeEnum.WRONG_NV_NAME;
            return false;
        }
        }
    }

    private boolean NV_NAMECheck() {
        while (index < jstr.length() && jstr.charAt(index) != '"') {
            if (jstr.charAt(index) == '\\') {
                if (!escapeCharacterTest()) {
                    errorType = ErrorTypeEnum.WRONG_ESC_STRING;
                    return false;
                }
            }
            index++;
        }
    
        if (index == jstr.length()) {
            errorType = ErrorTypeEnum.WRONG_NV_NAME;
            return false;
        }
    
        if (jstr.charAt(index) == '"') {
            sc.pop();
        } else {
            errorType = ErrorTypeEnum.WRONG_NV_NAME;
            return false;
        }
        index++;
        test = StatusEnum.COLON;
        return true;
    }
    private boolean COLONCheck() {
        while (index < jstr.length() && isSpace(jstr.charAt(index))) {
            index++;
        }

        if (index == jstr.length()) {
            errorType = ErrorTypeEnum.WRONG_MISS_COLON;
            return false;
        }

        if(jstr.charAt(index) != ':') {
            errorType = ErrorTypeEnum.WRONG_NV_NAME;
            return false;
        }

        index++;
        test = StatusEnum.VALUE;
        return true;
    }
    private boolean VALUECheck() {
        while (index < jstr.length() && isSpace(jstr.charAt(index))) {
            index++;
        }
    
        if (index == jstr.length()) {
            switch(sc.peek()) {
                case '{' :
                    errorType = ErrorTypeEnum.WRONG_NV_NAME;
                    break;
                case '[' :
                    errorType = ErrorTypeEnum.WRONG_NOT_MATCH;
            }
            return false;
        }
        preIndex = index;
        if (jstr.charAt(index) == '"') {
            test = StatusEnum.STRING;
        } else if (isDigit(jstr.charAt(index)) || jstr.charAt(index) == '-') {
            test = StatusEnum.NUMBER;
        } else if (jstr.charAt(index) == 'n') {
            test = StatusEnum.NULLSTR;
        } else if (jstr.charAt(index) == 't' || jstr.charAt(index) == 'f') {
            test = StatusEnum.BOOLSTR;
        } else if (jstr.charAt(index) == '{') {
            test = StatusEnum.OBJ_EMPTY;
            sc.push('{');
            //ss.push(OBJECT);
            index++;
        } else if (jstr.charAt(index) == '[') {
            test = StatusEnum.ARRAY_EMPTY;
            sc.push('[');
            //ss.push(ARRAY);
            index++;
        } else {
            errorType = ErrorTypeEnum.WRONG_VALUE_TYPE;
            return false;
        }
        return true;
    }

    private boolean ARRAY_EMPTYCheck() {
        while (index < jstr.length() && isSpace(jstr.charAt(index))) {
            index++;
        }

        if (index == jstr.length()) {
            errorType = ErrorTypeEnum.WRONG_NOT_MATCH;
            return false;
        }

        if (jstr.charAt(index) == ']') {
            sc.pop();
            //ss.pop();
            index++;
            if (sc.empty()) {
                test = StatusEnum.FINISH;
            }
            else {
                test = StatusEnum.VALUE_END;
            }
    
        } else {
            test = StatusEnum.VALUE;
        }
    
        return true;
    }

    private boolean NEXT_ELECheck() {

        if(sc.peek() == '{') {
            while (index < jstr.length() && isSpace(jstr.charAt(index))) {
                index++;
            }
            if (index == jstr.length()) {
                errorType = ErrorTypeEnum.WRONG_NOT_MATCH;
                return false;
            }
            if(jstr.charAt(index) == '"') {
                sc.push('"');
                test = StatusEnum.NV_NAME;
                index++;
                return true;
            }
            errorType = ErrorTypeEnum.WRONG_NV_NAME;
        }
        if(sc.peek() == '[') {
            test = StatusEnum.VALUE;
            return true;
        }
        index = preIndex;
        errorType = ErrorTypeEnum.WRONG_VALUE_TYPE;
        return false;
    }

    private boolean STRINGCheck() {
        sc.push('"');
        if (++index == jstr.length()) {
            errorType = ErrorTypeEnum.WRONG_NOT_MATCH;
            return false;
        }
    
        while (index < jstr.length() && jstr.charAt(index) != '"'){
            if (jstr.charAt(index) == '\\'){
                if (!escapeCharacterTest()) {
                    index = preIndex;
                    errorType = ErrorTypeEnum.WRONG_VALUE_TYPE;
                    return false;
                }
            }
            index++;
        }
    
        if (index == jstr.length()) {
            index = preIndex;
            errorType = ErrorTypeEnum.WRONG_VALUE_TYPE;
            return false;
        }
        
        sc.pop();
        index++;
        test = StatusEnum.VALUE_END;
        return true;
    }

    private boolean NUMBERCheck() {
        if (jstr.charAt(index) == '-') {

            if (++index == jstr.length()) {
                index = preIndex;
                errorType = ErrorTypeEnum.WRONG_VALUE_TYPE;
                return false;
            }
            if (!isDigit(jstr.charAt(index))) {
                index = preIndex;
                errorType = ErrorTypeEnum.WRONG_VALUE_TYPE;
                return false;
            }
        }
        if (jstr.charAt(index) == '0') {
            if (isDigit(jstr.charAt(index + 1))) {
                errorType = ErrorTypeEnum.WRONG_NUM;
                return false;
            }
        }
    
        while (index < jstr.length()) {
            if (isNumberEndFlag(jstr.charAt(index))) {
                test = StatusEnum.VALUE_END;
                return true;
            }
            else if (isDigit(jstr.charAt(index))) {
                index++;
            }
            else if (jstr.charAt(index) == '.') {
                if(index + 1 == jstr.length()) {
                    errorType = ErrorTypeEnum.WRONG_NUM;
                    return false;
                }
                index++;
                if(!isDigit(jstr.charAt(index))){
                    errorType = ErrorTypeEnum.WRONG_NUM;
                    return false;
                }
                while(index < jstr.length()) {
                    if(isDigit(jstr.charAt(index))) {
                        index++;
                    } else if(isNumberEndFlag(jstr.charAt(index))) {
                        test = StatusEnum.VALUE_END;
                        return true;
                    } else if(jstr.charAt(index ) == 'e' || jstr.charAt(index) == 'E') {
                        break;
                    } else {
                        errorType = ErrorTypeEnum.WRONG_NUM;
                        return false;
                    }
                }
            }
            else if (jstr.charAt(index) == 'e' || jstr.charAt(index) == 'E') {
                index++;
                if (jstr.charAt(index) == '\0') {
                    errorType = ErrorTypeEnum.WRONG_NUM;
                    return false;
                } else if (jstr.charAt(index) == '-' || jstr.charAt(index) == '+') {
                    index++;
                } else if (isDigit(jstr.charAt(index))) {
                } else {
                    errorType = ErrorTypeEnum.WRONG_NUM;
                    return false;
                }
    
                while (index < jstr.length()) {
                    if (isNumberEndFlag(jstr.charAt(index))) {
                        test = StatusEnum.VALUE_END;
                        return true;
                    }
                    else if (isDigit(jstr.charAt(index))) {
                        index++;
                    }
                    else {
                        errorType = ErrorTypeEnum.WRONG_NUM;
                        return false;
                    }
                }
            }
            else {
                index = preIndex;
                errorType = ErrorTypeEnum.WRONG_VALUE_TYPE;
                return false;
            }
        }
    
        test = StatusEnum.VALUE_END;
        return true;
    }

    private boolean BOOLSTRCheck() {
        String tmpStr = new String("");
        if (jstr.charAt(index) == 't') {
            for (int i = 0;i < 4;i++) {
                if (index < jstr.length()) {
                    tmpStr += jstr.charAt(index);
                    index++;
                } else {
                    index = preIndex;
                    errorType = ErrorTypeEnum.WRONG_VALUE_TYPE;
                    return false;
                }
            }
    
            if (tmpStr.equals("true")){
                test = StatusEnum.VALUE_END;
                return true;
            } else {
                index = preIndex;
                errorType = ErrorTypeEnum.WRONG_VALUE_TYPE;
                return false;
            }
        } else {
            for (int i = 0;i < 5; i++) {
                if (index < jstr.length()) {
                    tmpStr += jstr.charAt(index);
                    index++;
                } else {
                    index = preIndex;
                    errorType =ErrorTypeEnum. WRONG_VALUE_TYPE;
                    return false;
                }
            }
    
            if (tmpStr.equals("false")) {
                test = StatusEnum.VALUE_END;
                return true;
            } else {
                index = preIndex;
                errorType = ErrorTypeEnum.WRONG_VALUE_TYPE;
                return false;
            }
        }
    }

    private boolean NULLSTRCheck() {
        String tmpStr = new String("");
        for (int i = 0;i < 4;i++) {
            if ( index < jstr.length() ) {
                tmpStr += jstr.charAt(index);
                index++;
            } else {
                index = preIndex;
                errorType = ErrorTypeEnum.WRONG_VALUE_TYPE;
                return false;
            }
        }
    
        if (tmpStr.equals("null")) {
            test = StatusEnum.VALUE_END;
            return true;
        } else {
            index = preIndex;
            errorType = ErrorTypeEnum.WRONG_VALUE_TYPE;
            return false;
        }
    }
    private boolean VALUE_ENDCheck() {
        while(index < jstr.length() && isSpace(jstr.charAt(index))) {
                index++;
        }
        
        if(index == jstr.length()) {
            errorType = ErrorTypeEnum.WRONG_NOT_MATCH;
            return false;
        }
        
        if(jstr.charAt(index) == ',') {
            index++;
            test = StatusEnum.NEXT_ELE;
            return true;
        }
        if(jstr.charAt(index) == '}' && sc.peek() == '{') {
            sc.pop();
            index++;
            if(sc.empty()) {
                test = StatusEnum.FINISH;
                return true;
            } else {
                test = StatusEnum.VALUE_END;
                return true;
            }
        }
        if(jstr.charAt(index) == ']' && sc.peek() == '[') {
            sc.pop();
            index++;
            if(sc.empty()) {
                test = StatusEnum.FINISH;
                return true;
            } else {
                test = StatusEnum.VALUE_END;
                return true;
            }
        }
        errorType = ErrorTypeEnum.WRONG_MISS_DELIMITER;
        return false;
    }

    private boolean FINISHCheck() {
        while (index < jstr.length()) {
            if (isSpace(jstr.charAt(index))) {
                index++;
            } else {
                errorType = ErrorTypeEnum.WRONG_END;
                return false;
            }
        }
    
        return true;
    }

    private void printInfo() {

        System.out.println(jstr);
        for(int i = 0; i < index; i++) {
            System.out.print(' ');
        }
        System.out.println('^');
    }

    private boolean isSpace(char tmpc) {
        switch(tmpc){
            case ' ':
            case '\r':
            case '\n':
            case '\t':
            case '\f': return true;
            default: return false;
        }
    }
    
    boolean escapeCharacterTest() {
        if (++index == jstr.length()) {
            return false;
        }
        switch (jstr.charAt(index)) {
            case '\\':
            case '/':
            case 'b':
            case 'f':
            case 't':
            case 'n':
            case 'r':
            case '"': {
                return true;
            }
            case 'u': {
                for (int i = 0; i < 4; i++) {
                    index++;
                    if (index == jstr.length()) {
                        errorType = ErrorTypeEnum.WRONG_ESC_STRING;
                        return false;
                    }
                }
                return true;
            }
            default: {
                errorType = ErrorTypeEnum.WRONG_ESC_STRING;
                return false;
            }
        }
    }
    boolean isDigit(char tmpc) {
        if(tmpc >= '0' && tmpc <= '9') {
            return true;
        }
        return false;
    }

    boolean isNumberEndFlag(char tmpc) {
        switch (tmpc) {
            case ',':
            case '}':
            case ']':
            case ' ':
                return true;
            default:
                return false;
            }
    }
}
 