const carrossel = document.getElementById("carrossel");

const botoes = document.querySelectorAll(
    ".indicadores button"
);
const botaoatras = document.querySelectorAll(
    ".botaoatras button"
)

let atual = 0;
let crescido = 0;

function mudarCard(numero) {

    atual = numero;

    if (!carrossel) return;
    carrossel.style.transform =
        `translateX(-${atual * 100}%)`;


    botoes.forEach(botao => {
        botao.classList.remove("ativo");
    });


    if (botoes[atual]) botoes[atual].classList.add("ativo");
}
function crescer(numero){
    crescido = numero;

    carrossel.style.transform =
        `translateX(-${numero * 100}%)`;
    botaoatras.forEach(botao => {
            if(numero = 0)
                botao.classList.remove("ativo");
        }

    );
    if (numero >= numero){
        botaoatras[atual].classList.add("ativo");
    }


}


mudarCard(0);

/* =====================================================
   SEÇÃO VALORES — carrossel + cor por card (Inter.js)
   -----------------------------------------------------
   ★ ÁREA PARA EDITAR — BARRINHA ★
   Troque os valores abaixo para mudar a barrinha
   embaixo do título "Nossos Valores":
===================================================== */
const VALORES_BARRINHA_LARGURA = "140px"; /* ← LARGURA (ex: "100px", "200px") */
const VALORES_BARRINHA_ALTURA = "6px";    /* ← ALTURA/ESPESSURA (ex: "4px", "10px") */
const VALORES_BARRINHA_COR = "#1B4D3E";   /* ← COR (ex: "#E9AF22") */

/* Cores de cada valor: [frente, meio, trás]
   Ordem de exibição ao clicar → :
   Foco        = frente VERDE,  meio PRETO,   trás AMARELO
   Persistência = frente PRETO,  meio AMARELO, trás VERDE
   Esperança   = frente AMARELO, meio VERDE,   trás PRETO
   (as abas de trás giram junto: amarelo→preto→verde) */
const VALORES_CORES = [
    { frente: "#1E4D3B", meio: "#101010", tras: "#E0A81E" },
    { frente: "#101010", meio: "#E0A81E", tras: "#1E4D3B" },
    { frente: "#E9AF22", meio: "#1E4D3B", tras: "#101010" }
];

let valorAtual = 0;

function aplicarBarrinhaValores() {
    const barra = document.querySelector(".valores .linhahorisontal");
    if (!barra) return;
    barra.style.width = VALORES_BARRINHA_LARGURA;
    barra.style.height = VALORES_BARRINHA_ALTURA;
    barra.style.background = VALORES_BARRINHA_COR;
}

function mostrarValor(n) {
    const cards = document.querySelectorAll("#trilhoValores .valor-card");
    if (!cards.length) return;
    valorAtual = (n + cards.length) % cards.length;
    const combo = VALORES_CORES[valorAtual % VALORES_CORES.length];
    /* 1) mostra só o card da vez */
    cards.forEach((card, k) => {
        card.classList.toggle("is-ativo", k === valorAtual);
    });
    /* 2) pinta a FRENTE do card ativo */
    const ativo = cards[valorAtual];
    ativo.style.setProperty("background", combo.frente, "important");
    ativo.style.setProperty("background-color", combo.frente, "important");
    /* 3) pinta as ABAS DE TRÁS (giram junto: amarelo→preto→verde) */
    const abaMeio = document.getElementById("abaMeio");
    const abaTras = document.getElementById("abaTras");
    if (abaMeio) abaMeio.style.setProperty("background", combo.meio, "important");
    if (abaTras) abaTras.style.setProperty("background", combo.tras, "important");
}

function mudarValor(direcao) {
    mostrarValor(valorAtual + direcao);
}

/* liga os botões ← → sem precisar de onclick no HTML */
function ligarBotoesValores() {
    document.querySelectorAll("[data-valor-dir]").forEach((botao) => {
        botao.addEventListener("click", () => {
            mostrarValor(valorAtual + Number(botao.dataset.valorDir));
        });
    });
}

if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", () => {
        aplicarBarrinhaValores();
        mostrarValor(0);
        ligarBotoesValores();
    });
} else {
    aplicarBarrinhaValores();
    mostrarValor(0);
    ligarBotoesValores();
}
