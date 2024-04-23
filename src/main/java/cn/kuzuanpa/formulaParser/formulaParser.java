/*
 * This class was created by <Pactivsa>. It is a part of Thinker.
 * Get the Source Code in github:
 * https://github.com/kuzuanpa/Thinker
 *
 * Thinker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * Thinker is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 */
package cn.kuzuanpa.formulaParser;

import java.util.ArrayList;
import java.util.HashMap;
public class formulaParser {

    private final HashMap<String,Double> variables;
    public formulaParser(HashMap<String,Double> variables) {
        this.variables = variables;
    }
    public formulaParser(){
        this.variables = new HashMap<>();
    }
    public double eval(String str) {
        //分词
        ArrayList<tokenizer.Token> tokens = tokenizer.tokenize(str);
        //解析
        parser m_parser = new parser(this.variables);
        //输出运算结果
        return m_parser.parse(tokens);
    }
}
