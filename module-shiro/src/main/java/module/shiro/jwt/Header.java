package module.shiro.jwt;

import java.io.Serializable;

/**
 * @author lubin
 * @date 2021/7/27
 */
public class Header implements Serializable {

    private String alg;

    private String typ;

    public Header() {
    }

    public Header(String alg, String typ) {
        this.alg = alg;
        this.typ = typ;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Header && this.hashCode() == obj.hashCode() && this.alg != null && this.typ != null) {
            Header header = (Header) obj;
            return this.alg.equals(header.alg) && this.typ.equals(header.typ);
        }
        return Boolean.FALSE.booleanValue();
    }
}
