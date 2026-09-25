exports.getAppInfo = async (req, res) => {
    return res.status(200).json({ message: "Informações gerais do aplicativo." });
};