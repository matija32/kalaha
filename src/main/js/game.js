const React = require('react');
const ReactDOM = require('react-dom');
const axios = require('axios');


class Game extends React.Component {

    constructor(props) {
        super(props);
        this.state = {status: 'None'};
    }

    componentDidMount() {
        axios.get(`/api/status`)
            .then(res => {
                this.setState({ status : res.data});
            })
    }

    render() {
        return (
            <div>
                <h2>{this.state.status}</h2>
            </div>
        )
    }

}

ReactDOM.render(
    <Game />,
    document.getElementById('react')
)