const getAppInfo = async (req, res) => {
    return res.status(200).json({ success: true, message: "Informações gerais do aplicativo." });
};

const getAppStatus = async (req, res) => {
    return res.status(200).json({ success: true, message: "Status do sistema operacional." });
};

module.exports = {
    getAppInfo,
    getAppStatus
};