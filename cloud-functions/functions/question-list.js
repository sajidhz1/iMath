class Qs{
    getQs(){
        return {
            list: [
                {
                    id:"q1",
                    question:"What is one plus one?",
                    answer:2,
                    help:"example help",
                    solveMethod:"count one twice"
                },
                {
                    id: "q2",
                    question:"What is one plus two?",
                    answer:3,
                    help:"example help",
                    solveMethod:"count one thrice"
                }
            ]
        }
    }
}

module.exports = {Qs}